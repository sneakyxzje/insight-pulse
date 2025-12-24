<script lang="ts">
  let { children } = $props();
</script>

<main
  class="mr-3 my-3 h-[calc(100svh-1.5rem)] rounded-lg border border-sidebar-border bg-white px-8 py-8 overflow-hidden shadow-sm overflow-auto"
>
  {@render children()}
</main>
