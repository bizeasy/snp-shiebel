import { IGeo } from 'app/entities/geo/geo.model';
import { IGeoAssocType } from 'app/entities/geo-assoc-type/geo-assoc-type.model';

export interface IGeoAssoc {
  id: number;
  geo?: IGeo | null;
  geoTo?: IGeo | null;
  geoAssocType?: IGeoAssocType | null;
}

export type NewGeoAssoc = Omit<IGeoAssoc, 'id'> & { id: null };
