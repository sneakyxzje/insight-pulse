<script lang="ts">
  import { useCampaign } from "@src/lib/queries/campaign";
  import * as Card from "$lib/components/ui/card/index.js";
  import { Badge } from "$lib/components/ui/badge";
  import { Button } from "$lib/components/ui/button";
  import { Skeleton } from "$lib/components/ui/skeleton";
  import { Plus, Calendar, FileText, ArrowRight } from "lucide-svelte";
  import { goto } from "$app/navigation";
  import type { CampaignStatus } from "@src/lib/types/campaign";

  const formatDate = (dateString: string) => {
    if (!dateString) return "";
    return new Date(dateString).toLocaleDateString("vi-VN", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const campaigns = useCampaign();
  const statusColor: Record<CampaignStatus, string> = {
    ACTIVE: "bg-primary hover:bg-green-600",
    DRAFT: "bg-gray-500 hover:bg-gray-600",
    NEW: "bg-primary hover:bg-primary-600",
  };
</script>

<div class="space-y-6">
  <div class="flex items-center justify-between">
    <div>
      <h2 class="text-3xl font-bold tracking-tight">Chiến dịch</h2>
      <p class="text-muted-foreground mt-1">
        Quản lý và theo dõi các chiến dịch khảo sát của bạn.
      </p>
    </div>
    <Button class="cursor-pointer " onclick={() => goto("/campaigns/new")}>
      <Plus class="mr-2 h-4 w-4" />
      Tạo chiến dịch
    </Button>
  </div>

  {#if campaigns.isLoading}
    <div class="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
      {#each Array(3) as _}
        <div class="flex flex-col space-y-3">
          <Skeleton class="h-[180px] w-full rounded-xl" />
          <div class="space-y-2">
            <Skeleton class="h-4 w-[250px]" />
            <Skeleton class="h-4 w-[200px]" />
          </div>
        </div>
      {/each}
    </div>
  {:else if campaigns.isError}
    <div
      class="flex h-[300px] items-center justify-center rounded-lg border border-dashed border-red-200 bg-red-50 text-red-500"
    >
      <p>Không thể tải dữ liệu. Vui lòng thử lại sau.</p>
    </div>
  {:else if campaigns.data}
    {#if campaigns.data.length === 0}
      <div
        class="flex min-h-[400px] flex-col items-center justify-center rounded-lg border border-dashed text-center animate-in fade-in-50"
      >
        <div
          class="mx-auto flex h-20 w-20 items-center justify-center rounded-full bg-muted"
        >
          <FileText class="h-10 w-10 text-muted-foreground" />
        </div>
        <h3 class="mt-4 text-lg font-semibold">Chưa có chiến dịch nào</h3>
        <p class="mb-4 mt-2 text-sm text-muted-foreground max-w-sm">
          Bạn chưa tạo chiến dịch nào. Hãy bắt đầu thu thập dữ liệu ngay hôm
          nay.
        </p>
        <Button onclick={() => goto("/dashboard/campaigns/new")}>
          <Plus class="mr-2 h-4 w-4" />
          Tạo cái đầu tiên
        </Button>
      </div>
    {:else}
      <div class="grid gap-2 md:grid-cols-2 lg:grid-cols-3">
        {#each campaigns.data as item (item.id)}
          <Card.Root>
            <Card.Header>
              <div class="flex items-start justify-between">
                <Badge
                  class="{statusColor[item.status] ||
                    'bg-primary'} text-white border-0"
                >
                  {item.status}
                </Badge>
              </div>
              <Card.Title class="text-xl pt-2 " title={item.name}>
                {item.name}
              </Card.Title>
            </Card.Header>

            <Card.Content class="flex-1 pb-4">
              <p
                class="text-sm text-muted-foreground line-clamp-2 min-h-[2.5rem]"
              >
                {item.description || "Không có mô tả"}
              </p>

              <div class="mt-4 flex items-center text-xs text-muted-foreground">
                <Calendar class="mr-1 h-3 w-3" />
                <span>Tạo ngày: {formatDate(item.createdAt)}</span>
              </div>
            </Card.Content>

            <Card.Footer class="pt-0">
              <Button
                variant="outline"
                class="w-full cursor-pointer group-hover:border-primary group-hover:text-primary transition-colors"
                onclick={() => goto(`/campaigns/${item.id}`)}
              >
                Chi tiết
                <ArrowRight class="ml-2 h-4 w-4" />
              </Button>
            </Card.Footer>
          </Card.Root>
        {/each}
      </div>
    {/if}
  {/if}
</div>
