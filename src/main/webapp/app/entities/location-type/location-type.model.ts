export interface ILocationType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewLocationType = Omit<ILocationType, 'id'> & { id: null };
