import { IGeoType } from 'app/entities/geo-type/geo-type.model';

export interface IGeo {
  id: number;
  name?: string | null;
  code?: string | null;
  abbreviation?: string | null;
  geoType?: IGeoType | null;
}

export type NewGeo = Omit<IGeo, 'id'> & { id: null };
