import { Component, effect, inject, signal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { combineLatest, filter, map, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config';
import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { PartyIdentificationTypeDeleteDialog } from '../delete/party-identification-type-delete-dialog';
import { IPartyIdentificationType } from '../party-identification-type.model';
import { PartyIdentificationTypeService } from '../service/party-identification-type.service';

@Component({
  selector: 'sys-party-identification-type',
  templateUrl: './party-identification-type.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class PartyIdentificationType {
  readonly partyIdentificationTypes = signal<IPartyIdentificationType[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly partyIdentificationTypeService = inject(PartyIdentificationTypeService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.partyIdentificationTypeService.partyIdentificationTypesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly activatedRouteState = toSignal(
    combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      map(([queryParamMap, data]) => ({ queryParamMap, data })),
    ),
    { initialValue: { queryParamMap: this.activatedRoute.snapshot.queryParamMap, data: this.activatedRoute.snapshot.data } },
  );
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      this.partyIdentificationTypes.set(
        this.fillComponentAttributesFromResponseBody([...this.partyIdentificationTypeService.partyIdentificationTypes()]),
      );
    });
    effect(() => {
      const activatedRouteState = this.activatedRouteState();
      untracked(() => {
        // Only watch for route changes. Other signals should be ignored.
        this.fillComponentAttributeFromRoute(activatedRouteState.queryParamMap, activatedRouteState.data);
        this.load();
      });
    });
  }

  trackId = (item: IPartyIdentificationType): number => this.partyIdentificationTypeService.getPartyIdentificationTypeIdentifier(item);

  delete(partyIdentificationType: IPartyIdentificationType): void {
    const modalRef = this.modalService.open(PartyIdentificationTypeDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.partyIdentificationType = partyIdentificationType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected refineData(data: IPartyIdentificationType[]): IPartyIdentificationType[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IPartyIdentificationType[]): IPartyIdentificationType[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.partyIdentificationTypeService.partyIdentificationTypesParams.set(queryObject);
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
