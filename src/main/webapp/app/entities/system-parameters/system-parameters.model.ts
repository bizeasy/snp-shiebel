export interface ISystemParameters {
  id: number;
  key?: string | null;
  value?: string | null;
  description?: string | null;
  environment?: string | null;
}

export type NewSystemParameters = Omit<ISystemParameters, 'id'> & { id: null };
