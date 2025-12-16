export type FormQuestion = {
  id: string;
  type: "text" | "number" | "select" | "textarea";
  label: string;
  placeholder?: string;
  required: boolean;
  options?: string[];
};
