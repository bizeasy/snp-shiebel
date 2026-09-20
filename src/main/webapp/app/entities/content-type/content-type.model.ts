export interface IContentType {
  id: number;
  name?: string | null;
}

export type NewContentType = Omit<IContentType, 'id'> & { id: null };
