import type { FormQuestion } from "@src/lib/types/FormQuestion";

export const getMappedAnswers = (
  answer: Record<string, any>,
  formSchema?: FormQuestion[]
) => {
  if (!answer) return [];
  if (formSchema && formSchema.length > 0) {
    return formSchema.map((q) => ({
      label: q.label,
      value: answer[q.id] ?? "N/A",
    }));
  }
  return Object.entries(answer).map(([key, value]) => ({
    label: key,
    value: value ?? "N/A",
  }));
};

export const getRespondentName = (answerMap: any, schema: any[]) => {
  if (!answerMap || !schema || schema.length === 0) return "Ẩn danh";

  const firstQuestion = schema[0].id;

  const values = answerMap[firstQuestion];
  return values ? String(values) : "Chưa nhập";
};
