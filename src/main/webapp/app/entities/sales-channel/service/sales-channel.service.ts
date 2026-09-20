import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ISalesChannel, NewSalesChannel } from '../sales-channel.model';

export type PartialUpdateSalesChannel = Partial<ISalesChannel> & Pick<ISalesChannel, 'id'>;

@Service()
export class SalesChannelsService {
  readonly salesChannelsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly salesChannelsResource = httpResource<ISalesChannel[]>(() => {
    const params = this.salesChannelsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of salesChannel that have been fetched. It is updated when the salesChannelsResource emits a new value.
   * In case of error while fetching the salesChannels, the signal is set to an empty array.
   */
  readonly salesChannels = computed(() => (this.salesChannelsResource.hasValue() ? this.salesChannelsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/sales-channels`;
}

@Service()
export class SalesChannelService extends SalesChannelsService {
  protected readonly http = inject(HttpClient);

  create(salesChannel: NewSalesChannel): Observable<ISalesChannel> {
    return this.http.post<ISalesChannel>(this.resourceUrl, salesChannel);
  }

  update(salesChannel: ISalesChannel): Observable<ISalesChannel> {
    return this.http.put<ISalesChannel>(
      `${this.resourceUrl}/${encodeURIComponent(this.getSalesChannelIdentifier(salesChannel))}`,
      salesChannel,
    );
  }

  partialUpdate(salesChannel: PartialUpdateSalesChannel): Observable<ISalesChannel> {
    return this.http.patch<ISalesChannel>(
      `${this.resourceUrl}/${encodeURIComponent(this.getSalesChannelIdentifier(salesChannel))}`,
      salesChannel,
    );
  }

  find(id: number): Observable<ISalesChannel> {
    return this.http.get<ISalesChannel>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ISalesChannel[]>> {
    const options = createRequestOption(req);
    return this.http.get<ISalesChannel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getSalesChannelIdentifier(salesChannel: Pick<ISalesChannel, 'id'>): number {
    return salesChannel.id;
  }

  compareSalesChannel(o1: Pick<ISalesChannel, 'id'> | null, o2: Pick<ISalesChannel, 'id'> | null): boolean {
    return o1 && o2 ? this.getSalesChannelIdentifier(o1) === this.getSalesChannelIdentifier(o2) : o1 === o2;
  }

  addSalesChannelToCollectionIfMissing<Type extends Pick<ISalesChannel, 'id'>>(
    salesChannelCollection: Type[],
    ...salesChannelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const salesChannels: Type[] = salesChannelsToCheck.filter(
      salesChannelItem => salesChannelItem !== null && salesChannelItem !== undefined,
    );
    if (salesChannels.length > 0) {
      const salesChannelCollectionIdentifiers = salesChannelCollection.map(salesChannelItem =>
        this.getSalesChannelIdentifier(salesChannelItem),
      );
      const salesChannelsToAdd = salesChannels.filter(salesChannelItem => {
        const salesChannelIdentifier = this.getSalesChannelIdentifier(salesChannelItem);
        if (salesChannelCollectionIdentifiers.includes(salesChannelIdentifier)) {
          return false;
        }
        salesChannelCollectionIdentifiers.push(salesChannelIdentifier);
        return true;
      });
      return [...salesChannelsToAdd, ...salesChannelCollection];
    }
    return salesChannelCollection;
  }
}
