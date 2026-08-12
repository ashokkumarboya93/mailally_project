from __future__ import annotations

import html
import re
from pathlib import Path

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm
from reportlab.platypus import (
    Flowable,
    KeepTogether,
    ListFlowable,
    ListItem,
    PageBreak,
    Paragraph,
    Preformatted,
    SimpleDocTemplate,
    Spacer,
    Table,
    TableStyle,
)


ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "MAILALLY_COMPLETE_APPLICATION_ARCHITECTURE_GUIDE.md"
OUTPUT_DIR = ROOT / "output" / "pdf"
OUTPUT = OUTPUT_DIR / "MailAlly_Architecture_Readable_Notes.pdf"


def clean_text(value: str) -> str:
    replacements = {
        "\ufeff": "",
        "\u2014": " - ",
        "\u2013": " - ",
        "\u2022": "-",
        "\u2190": "<-",
        "\u2192": " -> ",
        "\u2193": "v",
        "\u2194": "<->",
        "\u2265": ">=",
        "\u2500": "-",
        "\u2502": "|",
        "\u250c": "+",
        "\u2510": "+",
        "\u2514": "+",
        "\u2518": "+",
        "\u251c": "+",
        "\u2524": "+",
        "\u252c": "+",
        "\u2534": "+",
        "\u253c": "+",
        "\u2550": "=",
        "\u25b2": "^",
        "\u25ba": ">",
        "\u25bc": "v",
        "\u25c4": "<",
        "\u2018": "'",
        "\u2019": "'",
        "\u201c": '"',
        "\u201d": '"',
        "\u2026": "...",
        "\u00a0": " ",
        "\u2713": "[done]",
        "\u2714": "[done]",
        "\u274c": "[x]",
        "\u26a0": "Warning:",
        "\u2705": "[done]",
        "\u25cf": "-",
        "\ufe0f": "",
    }
    for src, dst in replacements.items():
        value = value.replace(src, dst)
    return value


def inline_markdown(value: str) -> str:
    value = clean_text(value).strip()
    value = html.escape(value)
    value = re.sub(r"`([^`]+)`", r"<font face='Courier'>\1</font>", value)
    value = re.sub(r"\*\*([^*]+)\*\*", r"<b>\1</b>", value)
    value = re.sub(r"\*([^*]+)\*", r"<i>\1</i>", value)
    return value


def is_table_separator(line: str) -> bool:
    stripped = line.strip()
    if not (stripped.startswith("|") and stripped.endswith("|")):
        return False
    cells = [cell.strip() for cell in stripped.strip("|").split("|")]
    return bool(cells) and all(re.fullmatch(r":?-{3,}:?", cell or "") for cell in cells)


def table_rows(lines: list[str]) -> list[list[str]]:
    rows: list[list[str]] = []
    for line in lines:
        if is_table_separator(line):
            continue
        cells = [inline_markdown(cell.strip()) for cell in line.strip().strip("|").split("|")]
        rows.append(cells)
    return rows


class PageNumCanvas:
    def __init__(self, canvas, doc):
        self.canvas = canvas
        self.doc = doc

    def __call__(self, canvas, doc):
        canvas.saveState()
        width, height = A4
        canvas.setStrokeColor(colors.HexColor("#D9DEE7"))
        canvas.line(1.6 * cm, 1.35 * cm, width - 1.6 * cm, 1.35 * cm)
        canvas.setFillColor(colors.HexColor("#5C667A"))
        canvas.setFont("Helvetica", 8)
        canvas.drawString(1.6 * cm, 1.0 * cm, "MailAlly Architecture Notes")
        canvas.drawRightString(width - 1.6 * cm, 1.0 * cm, f"Page {doc.page}")
        canvas.restoreState()


def build_styles():
    base = getSampleStyleSheet()
    return {
        "title": ParagraphStyle(
            "Title",
            parent=base["Title"],
            fontName="Helvetica-Bold",
            fontSize=24,
            leading=30,
            textColor=colors.HexColor("#182033"),
            alignment=TA_CENTER,
            spaceAfter=18,
        ),
        "subtitle": ParagraphStyle(
            "Subtitle",
            parent=base["BodyText"],
            fontName="Helvetica",
            fontSize=11,
            leading=16,
            textColor=colors.HexColor("#4D5870"),
            alignment=TA_CENTER,
            spaceAfter=12,
        ),
        "h1": ParagraphStyle(
            "Heading1",
            parent=base["Heading1"],
            fontName="Helvetica-Bold",
            fontSize=17,
            leading=22,
            textColor=colors.HexColor("#17324D"),
            spaceBefore=16,
            spaceAfter=8,
            keepWithNext=True,
        ),
        "h2": ParagraphStyle(
            "Heading2",
            parent=base["Heading2"],
            fontName="Helvetica-Bold",
            fontSize=13,
            leading=17,
            textColor=colors.HexColor("#23527C"),
            spaceBefore=10,
            spaceAfter=6,
            keepWithNext=True,
        ),
        "h3": ParagraphStyle(
            "Heading3",
            parent=base["Heading3"],
            fontName="Helvetica-Bold",
            fontSize=11.5,
            leading=15,
            textColor=colors.HexColor("#334155"),
            spaceBefore=8,
            spaceAfter=4,
            keepWithNext=True,
        ),
        "body": ParagraphStyle(
            "Body",
            parent=base["BodyText"],
            fontName="Helvetica",
            fontSize=9.5,
            leading=13.5,
            textColor=colors.HexColor("#202938"),
            spaceAfter=5,
        ),
        "quote": ParagraphStyle(
            "Quote",
            parent=base["BodyText"],
            fontName="Helvetica-Oblique",
            fontSize=9,
            leading=13,
            leftIndent=10,
            rightIndent=10,
            borderPadding=7,
            borderColor=colors.HexColor("#CBD5E1"),
            borderWidth=0.5,
            backColor=colors.HexColor("#F6F8FB"),
            textColor=colors.HexColor("#364152"),
            spaceBefore=4,
            spaceAfter=7,
        ),
        "bullet": ParagraphStyle(
            "Bullet",
            parent=base["BodyText"],
            fontName="Helvetica",
            fontSize=9.2,
            leading=13,
            leftIndent=12,
            firstLineIndent=0,
            textColor=colors.HexColor("#202938"),
        ),
        "code": ParagraphStyle(
            "Code",
            parent=base["Code"],
            fontName="Courier",
            fontSize=7.2,
            leading=9.2,
            leftIndent=0,
            borderPadding=6,
            borderColor=colors.HexColor("#D8DEE9"),
            borderWidth=0.4,
            backColor=colors.HexColor("#F8FAFC"),
            textColor=colors.HexColor("#111827"),
            spaceBefore=4,
            spaceAfter=6,
        ),
        "toc": ParagraphStyle(
            "TOC",
            parent=base["BodyText"],
            fontName="Helvetica",
            fontSize=9.2,
            leading=12.5,
            textColor=colors.HexColor("#202938"),
            spaceAfter=2,
        ),
        "table": ParagraphStyle(
            "Table",
            parent=base["BodyText"],
            fontName="Helvetica",
            fontSize=7.4,
            leading=9.3,
            textColor=colors.HexColor("#1F2937"),
            wordWrap="CJK",
        ),
        "table_header": ParagraphStyle(
            "TableHeader",
            parent=base["BodyText"],
            fontName="Helvetica-Bold",
            fontSize=7.5,
            leading=9.5,
            textColor=colors.white,
            wordWrap="CJK",
        ),
    }


def make_table(lines: list[str], styles: dict[str, ParagraphStyle]) -> Flowable:
    rows = table_rows(lines)
    if not rows:
        return Spacer(1, 0)
    max_cols = max(len(row) for row in rows)
    normalized = [row + [""] * (max_cols - len(row)) for row in rows]
    data = []
    for row_index, row in enumerate(normalized):
        style = styles["table_header"] if row_index == 0 else styles["table"]
        data.append([Paragraph(cell or " ", style) for cell in row])
    usable_width = A4[0] - 3.2 * cm
    col_widths = [usable_width / max_cols] * max_cols
    table = Table(data, colWidths=col_widths, repeatRows=1, hAlign="LEFT")
    table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#23527C")),
                ("BACKGROUND", (0, 1), (-1, -1), colors.HexColor("#FFFFFF")),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#F8FAFC")]),
                ("GRID", (0, 0), (-1, -1), 0.35, colors.HexColor("#CBD5E1")),
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("LEFTPADDING", (0, 0), (-1, -1), 5),
                ("RIGHTPADDING", (0, 0), (-1, -1), 5),
                ("TOPPADDING", (0, 0), (-1, -1), 4),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 4),
            ]
        )
    )
    return table


def flush_paragraph(buffer: list[str], story: list[Flowable], styles: dict[str, ParagraphStyle]) -> None:
    if not buffer:
        return
    text = " ".join(line.strip() for line in buffer if line.strip())
    if text:
        story.append(Paragraph(inline_markdown(text), styles["body"]))
    buffer.clear()


def flush_bullets(buffer: list[str], story: list[Flowable], styles: dict[str, ParagraphStyle]) -> None:
    if not buffer:
        return
    items = [
        ListItem(Paragraph(inline_markdown(item), styles["bullet"]), leftIndent=8)
        for item in buffer
        if item.strip()
    ]
    if items:
        story.append(ListFlowable(items, bulletType="bullet", leftIndent=16, bulletFontSize=6))
        story.append(Spacer(1, 3))
    buffer.clear()


def flush_table(buffer: list[str], story: list[Flowable], styles: dict[str, ParagraphStyle]) -> None:
    if not buffer:
        return
    story.append(make_table(buffer, styles))
    story.append(Spacer(1, 7))
    buffer.clear()


def parse_markdown(text: str, styles: dict[str, ParagraphStyle]) -> list[Flowable]:
    story: list[Flowable] = []
    para: list[str] = []
    bullets: list[str] = []
    table: list[str] = []
    code: list[str] = []
    in_code = False

    for raw_line in text.splitlines():
        line = clean_text(raw_line.rstrip())

        if line.strip().startswith("```"):
            flush_paragraph(para, story, styles)
            flush_bullets(bullets, story, styles)
            flush_table(table, story, styles)
            if in_code:
                code_text = "\n".join(code).strip("\n")
                if code_text:
                    story.append(Preformatted(code_text, styles["code"], maxLineLength=96))
                code.clear()
                in_code = False
            else:
                in_code = True
            continue

        if in_code:
            code.append(line)
            continue

        if not line.strip():
            flush_paragraph(para, story, styles)
            flush_bullets(bullets, story, styles)
            flush_table(table, story, styles)
            continue

        if line.strip() == "---":
            flush_paragraph(para, story, styles)
            flush_bullets(bullets, story, styles)
            flush_table(table, story, styles)
            story.append(Spacer(1, 6))
            continue

        if line.lstrip().startswith("|") and line.rstrip().endswith("|"):
            flush_paragraph(para, story, styles)
            flush_bullets(bullets, story, styles)
            table.append(line)
            continue
        else:
            flush_table(table, story, styles)

        heading = re.match(r"^(#{1,6})\s+(.+)$", line)
        if heading:
            flush_paragraph(para, story, styles)
            flush_bullets(bullets, story, styles)
            level = len(heading.group(1))
            title = inline_markdown(heading.group(2))
            if level == 1 and story:
                story.append(PageBreak())
            style = styles["h1"] if level == 1 else styles["h2"] if level == 2 else styles["h3"]
            story.append(Paragraph(title, style))
            continue

        bullet = re.match(r"^\s*(?:[-*]|\d+\.)\s+(.+)$", line)
        if bullet:
            flush_paragraph(para, story, styles)
            bullets.append(bullet.group(1))
            continue
        else:
            flush_bullets(bullets, story, styles)

        if line.strip().startswith(">"):
            flush_paragraph(para, story, styles)
            story.append(Paragraph(inline_markdown(line.strip().lstrip(">").strip()), styles["quote"]))
            continue

        para.append(line)

    flush_paragraph(para, story, styles)
    flush_bullets(bullets, story, styles)
    flush_table(table, story, styles)
    return story


def extract_toc(text: str) -> list[str]:
    toc: list[str] = []
    in_toc = False
    for line in text.splitlines():
        cleaned = clean_text(line)
        if cleaned.strip().upper() == "# TABLE OF CONTENTS":
            in_toc = True
            continue
        if in_toc and cleaned.startswith("# PART 1"):
            break
        if in_toc and cleaned.strip().startswith("|") and not is_table_separator(cleaned):
            cells = [cell.strip() for cell in cleaned.strip().strip("|").split("|")]
            if len(cells) == 2 and cells[0].lower() != "part":
                toc.append(f"Part {cells[0]} - {cells[1]}")
    return toc


def build_pdf() -> None:
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    raw = SOURCE.read_text(encoding="utf-8-sig")
    text = clean_text(raw)
    styles = build_styles()

    doc = SimpleDocTemplate(
        str(OUTPUT),
        pagesize=A4,
        rightMargin=1.6 * cm,
        leftMargin=1.6 * cm,
        topMargin=1.55 * cm,
        bottomMargin=1.75 * cm,
        title="MailAlly Architecture Readable Notes",
        author="MailAlly",
    )

    story: list[Flowable] = [
        Spacer(1, 3.2 * cm),
        Paragraph("MailAlly Architecture", styles["title"]),
        Paragraph("Readable Notes from the Complete Application Architecture Guide", styles["subtitle"]),
        Spacer(1, 0.6 * cm),
        Paragraph(
            "Generated as a clean, long-form PDF for study, review, and technical walkthroughs. "
            "The source guide content is preserved and reformatted with readable spacing, tables, "
            "code blocks, section breaks, and page numbers.",
            styles["subtitle"],
        ),
        PageBreak(),
        Paragraph("Study Map", styles["h1"]),
    ]

    toc_items = extract_toc(text)
    for item in toc_items:
        story.append(Paragraph(inline_markdown(item), styles["toc"]))
    story.append(PageBreak())

    content = re.sub(r"(?s)^# TABLE OF CONTENTS.*?(?=^# PART 1)", "", text, count=1, flags=re.MULTILINE)
    story.extend(parse_markdown(content, styles))

    doc.build(story, onFirstPage=PageNumCanvas(None, None), onLaterPages=PageNumCanvas(None, None))


if __name__ == "__main__":
    build_pdf()
    print(OUTPUT)
