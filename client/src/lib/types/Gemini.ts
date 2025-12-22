export interface GeminiComparisonResponse {
  aiAssesment: string;
  summary: string;
  score: number;
  highlights: {
    text: string;
    type: "positive" | "negative" | "neutral";
    comment: string;
  }[];
}
