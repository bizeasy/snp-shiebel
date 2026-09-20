export interface IGeoAssocType {
  id: number;
  name?: string | null;
}

export type NewGeoAssocType = Omit<IGeoAssocType, 'id'> & { id: null };
