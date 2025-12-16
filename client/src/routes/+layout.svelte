<script lang="ts">
  import "./layout.css";
  import favicon from "$lib/assets/favicon.svg";
  import { QueryClient, QueryClientProvider } from "@tanstack/svelte-query";
  import { SvelteQueryDevtools } from "@tanstack/svelte-query-devtools";
  import { ModeWatcher } from "mode-watcher";
  import { browser } from "$app/environment";

  let { children } = $props();

  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        enabled: browser,
        staleTime: 60_000,
      },
    },
  });
</script>

<svelte:head><link rel="icon" href={favicon} /></svelte:head>

<ModeWatcher />

<QueryClientProvider client={queryClient}>
  {@render children()}
  <SvelteQueryDevtools buttonPosition="bottom-right" />
</QueryClientProvider>
