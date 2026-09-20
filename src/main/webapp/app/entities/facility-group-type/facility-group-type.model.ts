export interface IFacilityGroupType {
  id: number;
  name?: string | null;
  description?: string | null;
}

export type NewFacilityGroupType = Omit<IFacilityGroupType, 'id'> & { id: null };
