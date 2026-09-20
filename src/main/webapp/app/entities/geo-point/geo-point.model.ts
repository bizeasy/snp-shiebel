export interface IGeoPoint {
  id: number;
  latitude?: number | null;
  longitude?: number | null;
}

export type NewGeoPoint = Omit<IGeoPoint, 'id'> & { id: null };
