export interface IKeywordType {
  id: number;
  name?: string | null;
}

export type NewKeywordType = Omit<IKeywordType, 'id'> & { id: null };
