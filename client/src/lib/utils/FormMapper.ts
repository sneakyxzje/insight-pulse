import type { FormQuestion } from "@src/lib/types/FormQuestion";

export const getMappedAnswers = (
  formSchema: FormQuestion[],
  answer: Record<string, any>
) => {
  return formSchema.map((q) => ({
    label: q.label,

    value: answer[q.id] ?? "N/A",
  }));
};
