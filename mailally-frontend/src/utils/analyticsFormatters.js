const DEFAULT_ANALYTICS_DETAIL = {
  hasData: false,
  campaignName: 'All Organization Campaigns',
  campaignStatus: 'ACTIVE',
  healthScore: 0,
  healthRating: 'NO_DATA',
  healthSummary: 'No campaign engagement events recorded yet.',
  benchmarks: [],
  campaignSummary: { totalRecipients: 0, sent: 0, delivered: 0, failed: 0, queued: 0, sending: 0 },
  deliveryFunnel: { queued: 0, sent: 0, delivered: 0, opened: 0, clicked: 0, sentPct: 0, deliveredPct: 0, openPct: 0, clickPct: 0 },
  kpis: { deliveryRate: 0, openRate: 0, clickRate: 0, bounceRate: 0, complaintRate: 0, unsubscribeRate: 0 },
  recipientActivities: [],
  liveActivityFeed: []
};

export const numberOrZero = (value) => {
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : 0;
};

export const formatPercent = (value, digits = 0) => {
  const normalized = numberOrZero(value);
  return `${digits > 0 ? normalized.toFixed(digits) : normalized}%`;
};

export const normalizeBenchmark = (benchmark = {}) => ({
  metricName: benchmark.metricName || benchmark.name || 'Metric',
  actualRate: numberOrZero(benchmark.actualRate ?? benchmark.campaignValue),
  benchmarkRate: numberOrZero(benchmark.benchmarkRate ?? benchmark.benchmarkValue),
  variancePct: numberOrZero(benchmark.variancePct ?? benchmark.diffPercentage),
  favorable: Boolean(benchmark.favorable ?? benchmark.isPositive ?? benchmark.positive)
});

export const normalizeAnalyticsDetail = (detail = {}, campaignName) => {
  const merged = {
    ...DEFAULT_ANALYTICS_DETAIL,
    ...detail,
    campaignName: detail.campaignName || campaignName || DEFAULT_ANALYTICS_DETAIL.campaignName,
    campaignSummary: { ...DEFAULT_ANALYTICS_DETAIL.campaignSummary, ...(detail.campaignSummary || {}) },
    deliveryFunnel: { ...DEFAULT_ANALYTICS_DETAIL.deliveryFunnel, ...(detail.deliveryFunnel || {}) },
    kpis: { ...DEFAULT_ANALYTICS_DETAIL.kpis, ...(detail.kpis || {}) },
    benchmarks: (detail.benchmarks || []).map(normalizeBenchmark),
    recipientActivities: detail.recipientActivities || [],
    liveActivityFeed: detail.liveActivityFeed || []
  };

  merged.healthScore = numberOrZero(merged.healthScore);
  return merged;
};

export const normalizeCampaignMetric = (campaign = {}) => ({
  ...campaign,
  campaignName: campaign.campaignName || 'Untitled Campaign',
  status: campaign.status || 'UNKNOWN',
  sentCount: numberOrZero(campaign.sentCount),
  openRate: numberOrZero(campaign.openRate),
  clickRate: numberOrZero(campaign.clickRate),
  bounceRate: numberOrZero(campaign.bounceRate)
});
