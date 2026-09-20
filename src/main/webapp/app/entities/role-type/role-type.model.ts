export interface IRoleType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewRoleType = Omit<IRoleType, 'id'> & { id: null };
