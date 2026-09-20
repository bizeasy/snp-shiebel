export interface ISalesChannel {
  id: number;
  name?: string | null;
  description?: string | null;
  sequenceNo?: number | null;
}

export type NewSalesChannel = Omit<ISalesChannel, 'id'> & { id: null };
