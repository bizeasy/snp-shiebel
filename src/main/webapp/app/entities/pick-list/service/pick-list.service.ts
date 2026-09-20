import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPickList, NewPickList } from '../pick-list.model';

export type PartialUpdatePickList = Partial<IPickList> & Pick<IPickList, 'id'>;

type RestOf<T extends IPickList | NewPickList> = Omit<T, 'pickListDate'> & {
  pickListDate?: string | null;
};

export type RestPickList = RestOf<IPickList>;

export type NewRestPickList = RestOf<NewPickList>;

export type PartialUpdateRestPickList = RestOf<PartialUpdatePickList>;

@Service()
export class PickListsService {
  readonly pickListsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly pickListsResource = httpResource<RestPickList[]>(() => {
    const params = this.pickListsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of pickList that have been fetched. It is updated when the pickListsResource emits a new value.
   * In case of error while fetching the pickLists, the signal is set to an empty array.
   */
  readonly pickLists = computed(() =>
    (this.pickListsResource.hasValue() ? this.pickListsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/pick-lists`;

  protected convertValueFromServer(restPickList: RestPickList): IPickList {
    return {
      ...restPickList,
      pickListDate: restPickList.pickListDate ? dayjs(restPickList.pickListDate) : undefined,
    };
  }
}

@Service()
export class PickListService extends PickListsService {
  protected readonly http = inject(HttpClient);

  create(pickList: NewPickList): Observable<IPickList> {
    const copy = this.convertValueFromClient(pickList);
    return this.http.post<RestPickList>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(pickList: IPickList): Observable<IPickList> {
    const copy = this.convertValueFromClient(pickList);
    return this.http
      .put<RestPickList>(`${this.resourceUrl}/${encodeURIComponent(this.getPickListIdentifier(pickList))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(pickList: PartialUpdatePickList): Observable<IPickList> {
    const copy = this.convertValueFromClient(pickList);
    return this.http
      .patch<RestPickList>(`${this.resourceUrl}/${encodeURIComponent(this.getPickListIdentifier(pickList))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPickList> {
    return this.http
      .get<RestPickList>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPickList[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPickList[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPickListIdentifier(pickList: Pick<IPickList, 'id'>): number {
    return pickList.id;
  }

  comparePickList(o1: Pick<IPickList, 'id'> | null, o2: Pick<IPickList, 'id'> | null): boolean {
    return o1 && o2 ? this.getPickListIdentifier(o1) === this.getPickListIdentifier(o2) : o1 === o2;
  }

  addPickListToCollectionIfMissing<Type extends Pick<IPickList, 'id'>>(
    pickListCollection: Type[],
    ...pickListsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pickLists: Type[] = pickListsToCheck.filter(pickListItem => pickListItem !== null && pickListItem !== undefined);
    if (pickLists.length > 0) {
      const pickListCollectionIdentifiers = pickListCollection.map(pickListItem => this.getPickListIdentifier(pickListItem));
      const pickListsToAdd = pickLists.filter(pickListItem => {
        const pickListIdentifier = this.getPickListIdentifier(pickListItem);
        if (pickListCollectionIdentifiers.includes(pickListIdentifier)) {
          return false;
        }
        pickListCollectionIdentifiers.push(pickListIdentifier);
        return true;
      });
      return [...pickListsToAdd, ...pickListCollection];
    }
    return pickListCollection;
  }

  protected convertValueFromClient<T extends IPickList | NewPickList | PartialUpdatePickList>(pickList: T): RestOf<T> {
    return {
      ...pickList,
      pickListDate: pickList.pickListDate?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPickList): IPickList {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPickList[]): IPickList[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
