<script lang="ts">
  import FormBuilder from "@src/lib/components/FormBuilder.svelte";
  import Button from "@src/lib/components/ui/button/button.svelte";
  import Input from "@src/lib/components/ui/input/input.svelte";
  import Label from "@src/lib/components/ui/label/label.svelte";
  import * as Card from "$lib/components/ui/card/index.js";
  import { Save } from "lucide-svelte";
  import { createMutation, useQueryClient } from "@tanstack/svelte-query";
  import { createCampaign } from "@src/lib/api/campaign";
  import { goto } from "$app/navigation";
  import type { CreateCampaignRequest } from "@src/lib/types/CreateCampaignRequest";

  const queryClient = useQueryClient();
  let formData = $state<CreateCampaignRequest>({
    name: "",
    description: "",
    aiSystemPrompt: "",
    formSchema: [],
  });
  const mutation = createMutation(() => ({
    mutationFn: (payload: CreateCampaignRequest) => createCampaign(payload),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["campaign"] });
      goto("/campaigns");
    },
    onError: (error: any) => {
      console.error(error);
    },
  }));

  const handleSubmit = () => {
    mutation.mutate(formData);
    console.log(formData);
  };
</script>

<div class="container max-w-4xl py-10 space-y-8">
  <div class="flex items-center justify-between">
    <div>
      <h2 class="text-3xl font-bold tracking-tight">Tạo chiến dịch mới</h2>
      <p class="text-muted-foreground mt-1">
        Thiết lập thông tin và câu hỏi cho chiến dịch khảo sát.
      </p>
    </div>
    <Button>
      <Save class="mr-2 h-4 w-4" /> Lưu chiến dịch
    </Button>
  </div>

  <div class="grid gap-8">
    <Card.Root>
      <Card.Header>
        <Card.Title>1. Thông tin cơ bản</Card.Title>
        <Card.Description
          >Các thông tin này sẽ hiển thị ở trang bắt đầu khảo sát.</Card.Description
        >
      </Card.Header>
      <Card.Content class="space-y-6">
        <div class="space-y-2">
          <Label for="name"
            >Tên chiến dịch <span class="text-red-500">*</span></Label
          >
          <Input
            id="name"
            placeholder="VD: Khảo sát mức độ hài lòng nhân viên..."
            bind:value={formData.name}
          />
        </div>

        <div class="space-y-2">
          <Label for="description">Mô tả ngắn</Label>
          <Input
            id="description"
            placeholder="Mô tả mục đích của khảo sát này..."
            bind:value={formData.description}
          />
        </div>

        <div class="space-y-2">
          <Label for="aiSystemPrompt">AI Prompt (Luật chấm điểm)</Label>
          <div class="text-xs text-muted-foreground mb-1">
            Hướng dẫn cho AI cách chấm điểm và phân tích câu trả lời.
          </div>
          <textarea
            id="aiSystemPrompt"
            class="flex min-h-[100px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
            placeholder="VD: Bạn là chuyên gia tuyển dụng. Hãy chấm điểm ứng viên dựa trên tư duy logic..."
            bind:value={formData.aiSystemPrompt}
          ></textarea>
        </div>
      </Card.Content>
    </Card.Root>

    <Card.Root>
      <Card.Header>
        <Card.Title>2. Thiết kế bộ câu hỏi</Card.Title>
        <Card.Description>Kéo thả để sắp xếp thứ tự câu hỏi.</Card.Description>
      </Card.Header>
      <Card.Content>
        <FormBuilder bind:schema={formData.formSchema} />
        <div
          class="mt-10 p-4 bg-slate-900 text-green-400 rounded-lg font-mono text-xs overflow-auto"
        >
          <h3 class="font-bold text-white mb-2">LIVE DATA CHECK:</h3>
          <pre>{JSON.stringify(formData, null, 2)}</pre>
        </div>
      </Card.Content>
    </Card.Root>
  </div>

  <div class="flex justify-end gap-4">
    <Button variant="outline">Hủy bỏ</Button>
    <Button onclick={handleSubmit}>Hoàn tất & Xuất bản</Button>
  </div>
</div>
