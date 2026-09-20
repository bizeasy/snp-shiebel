export interface ICatalogueCategoryType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewCatalogueCategoryType = Omit<ICatalogueCategoryType, 'id'> & { id: null };
