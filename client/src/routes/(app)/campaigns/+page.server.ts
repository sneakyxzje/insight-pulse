import { api } from "@src/lib/utils/api.js";
import type { PageServerLoad } from "./$types";
import type { Campaign } from "@src/lib/types/campaign";

export const load: PageServerLoad = async ({ fetch }) => {
  const res = await api.get<Campaign[]>("/campaigns", fetch);

  return {
    campaigns: res,
  };
};
