export const getScoreColor = (score: number) => {
  if (!score) return "text-slate-500 bg-slate-100 border-slate-200";
  if (score >= 8) return "text-green-700 bg-green-50 border-green-200";
  if (score >= 5) return "text-yellow-700 bg-yellow-50 border-yellow-200";
  return "text-red-700 bg-red-50 border-red-200";
};
