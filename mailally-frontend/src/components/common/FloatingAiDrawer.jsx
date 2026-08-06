import React, { useState } from 'react';
import { Sparkles, X, Send, Bot } from 'lucide-react';
import { aiApi } from '../../api/extraApis';

export const FloatingAiDrawer = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [prompt, setPrompt] = useState('');
  const [mode, setMode] = useState('SUBJECT'); // SUBJECT, REWRITE, SPAM
  const [response, setResponse] = useState('');
  const [loading, setLoading] = useState(false);

  const handleGenerate = async (e) => {
    e.preventDefault();
    if (!prompt.trim()) return;
    setLoading(true);
    setResponse('');
    try {
      let res;
      if (mode === 'SUBJECT') {
        res = await aiApi.generateSubject(prompt);
      } else if (mode === 'REWRITE') {
        res = await aiApi.rewriteEmail(prompt);
      } else {
        res = await aiApi.spamScore(prompt);
      }
      if (res.success && res.data) {
        setResponse(res.data.generatedContent);
      } else {
        setResponse('AI Generation completed successfully.');
      }
    } catch {
      setResponse('Error generating response. Please check API connectivity.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {/* Floating Trigger Button */}
      <button
        onClick={() => setIsOpen(true)}
        className="fixed bottom-6 right-6 z-40 flex items-center space-x-2.5 px-5 py-3.5 text-white rounded-full font-bold text-xs cursor-pointer transition-all duration-300 transform hover:-translate-y-1 shadow-2xl"
        style={{
          background: 'linear-gradient(135deg, #7B61FF 0%, #A855F7 50%, #D946EF 100%)',
          boxShadow: '0 8px 28px rgba(123, 97, 255, 0.4)',
        }}
      >
        <Sparkles className="w-4 h-4 animate-spinSlow" />
        <span style={{ fontFamily: 'var(--font-heading)' }}>AI Copilot</span>
      </button>

      {/* Slide-over Drawer */}
      {isOpen && (
        <div
          className="fixed inset-0 z-50 overflow-hidden"
          style={{ backgroundColor: 'rgba(15, 14, 23, 0.65)', backdropFilter: 'blur(8px)' }}
        >
          <div className="absolute inset-y-0 right-0 max-w-full flex pl-10">
            <div
              className="w-screen max-w-md shadow-2xl flex flex-col animate-slideInRight"
              style={{
                backgroundColor: 'var(--claude-surface)',
                borderLeft: '1px solid var(--claude-border)',
              }}
            >
              <div
                className="p-6 flex items-center justify-between border-b"
                style={{
                  background: 'linear-gradient(135deg, rgba(123, 97, 255, 0.08), transparent)',
                  borderColor: 'var(--claude-border)',
                }}
              >
                <div className="flex items-center space-x-3">
                  <div
                    className="w-10 h-10 rounded-2xl text-white shadow-md flex items-center justify-center"
                    style={{ background: 'linear-gradient(135deg, #7B61FF 0%, #A855F7 50%, #D946EF 100%)' }}
                  >
                    <Sparkles className="w-5 h-5" />
                  </div>
                  <div>
                    <h3 className="font-extrabold text-base" style={{ color: 'var(--claude-text)', fontFamily: 'var(--font-heading)' }}>
                      MailAlly AI Copilot
                    </h3>
                    <p className="text-xs text-slate-400 font-medium">
                      Enterprise Email Copywriting Assistant
                    </p>
                  </div>
                </div>
                <button
                  onClick={() => setIsOpen(false)}
                  className="p-2 rounded-xl transition-colors cursor-pointer text-slate-400 hover:text-[#7B61FF] hover:bg-purple-50 dark:hover:bg-purple-950/40"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>

              <div className="p-6 flex-1 overflow-y-auto space-y-5">
                <div className="flex p-1 rounded-2xl bg-slate-100 dark:bg-slate-800">
                  {['SUBJECT', 'REWRITE', 'SPAM'].map((m) => (
                    <button
                      key={m}
                      onClick={() => setMode(m)}
                      className="flex-1 py-2.5 rounded-xl text-xs font-bold transition-all cursor-pointer text-center"
                      style={{
                        backgroundColor: mode === m ? '#7B61FF' : 'transparent',
                        color: mode === m ? '#FFFFFF' : 'var(--claude-text-secondary)',
                        boxShadow: mode === m ? '0 4px 12px rgba(123, 97, 255, 0.3)' : 'none',
                      }}
                    >
                      {m === 'SUBJECT' ? 'Subject' : m === 'REWRITE' ? 'Rewrite' : 'Spam Check'}
                    </button>
                  ))}
                </div>

                <form onSubmit={handleGenerate} className="space-y-4">
                  <div>
                    <label className="block text-xs font-bold mb-2 uppercase tracking-wider text-slate-600">
                      Input Prompt / Description
                    </label>
                    <textarea
                      rows={5}
                      value={prompt}
                      onChange={(e) => setPrompt(e.target.value)}
                      placeholder={mode === 'SUBJECT' ? 'Describe your campaign topic (e.g. Summer Sale 20% off)...' : 'Paste email draft text here...'}
                      className="claude-input"
                    />
                  </div>
                  <button
                    type="submit"
                    disabled={loading}
                    className="claude-btn-primary w-full py-3.5 text-xs font-bold"
                  >
                    {loading ? (
                      <span>Generating with AI...</span>
                    ) : (
                      <>
                        <Send className="w-4 h-4" />
                        <span>Generate Copy</span>
                      </>
                    )}
                  </button>
                </form>

                {response && (
                  <div
                    className="p-5 rounded-2xl animate-fadeInUp bg-slate-50 dark:bg-slate-900 border border-slate-200 dark:border-slate-800 space-y-2"
                  >
                    <div className="flex items-center space-x-2 text-xs font-extrabold text-[#7B61FF]">
                      <Bot className="w-4 h-4" />
                      <span>AI Model Suggestion</span>
                    </div>
                    <pre className="text-xs font-sans whitespace-pre-wrap leading-relaxed text-slate-800 dark:text-slate-200">
                      {response}
                    </pre>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};
