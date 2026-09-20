export interface IGeoType {
  id: number;
  name?: string | null;
}

export type NewGeoType = Omit<IGeoType, 'id'> & { id: null };
