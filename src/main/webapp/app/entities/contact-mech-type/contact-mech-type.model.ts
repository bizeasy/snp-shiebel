export interface IContactMechType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewContactMechType = Omit<IContactMechType, 'id'> & { id: null };
