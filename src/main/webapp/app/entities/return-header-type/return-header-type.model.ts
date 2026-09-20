export interface IReturnHeaderType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewReturnHeaderType = Omit<IReturnHeaderType, 'id'> & { id: null };
