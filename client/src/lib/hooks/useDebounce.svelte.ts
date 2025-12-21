export function useDebounce<T>(valueGetter: () => T, delay: number = 500) {
  let debounced = $state(valueGetter());
  $effect(() => {
    const value = valueGetter();

    const handler = setTimeout(() => {
      debounced = value;
    }, delay);

    return () => clearTimeout(handler);
  });

  return {
    get current() {
      return debounced;
    },
  };
}
