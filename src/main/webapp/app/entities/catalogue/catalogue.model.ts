export interface ICatalogue {
  id: number;
  name?: string | null;
  description?: string | null;
  sequenceNo?: number | null;
  imagePath?: string | null;
  mobileImagePath?: string | null;
  altImage1?: string | null;
  altImage2?: string | null;
  altImage3?: string | null;
  bannerUrl?: string | null;
  isBanner?: boolean | null;
}

export type NewCatalogue = Omit<ICatalogue, 'id'> & { id: null };
