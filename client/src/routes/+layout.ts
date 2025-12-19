import { QueryClient } from "@tanstack/svelte-query";
import type { LayoutLoad } from "./$types";

export const load: LayoutLoad = async ({ data }) => {
  const queryClient = new QueryClient();

  return { ...data, queryClient };
};
