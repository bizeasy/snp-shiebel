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
import { TaxAuthorityRateTypeDeleteDialog } from '../delete/tax-authority-rate-type-delete-dialog';
import { TaxAuthorityRateTypeService } from '../service/tax-authority-rate-type.service';
import { ITaxAuthorityRateType } from '../tax-authority-rate-type.model';

@Component({
  selector: 'sys-tax-authority-rate-type',
  templateUrl: './tax-authority-rate-type.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class TaxAuthorityRateType {
  readonly taxAuthorityRateTypes = signal<ITaxAuthorityRateType[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly taxAuthorityRateTypeService = inject(TaxAuthorityRateTypeService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.taxAuthorityRateTypeService.taxAuthorityRateTypesResource.isLoading;
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
      this.taxAuthorityRateTypes.set(
        this.fillComponentAttributesFromResponseBody([...this.taxAuthorityRateTypeService.taxAuthorityRateTypes()]),
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

  trackId = (item: ITaxAuthorityRateType): number => this.taxAuthorityRateTypeService.getTaxAuthorityRateTypeIdentifier(item);

  delete(taxAuthorityRateType: ITaxAuthorityRateType): void {
    const modalRef = this.modalService.open(TaxAuthorityRateTypeDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.taxAuthorityRateType = taxAuthorityRateType;
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

  protected refineData(data: ITaxAuthorityRateType[]): ITaxAuthorityRateType[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: ITaxAuthorityRateType[]): ITaxAuthorityRateType[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.taxAuthorityRateTypeService.taxAuthorityRateTypesParams.set(queryObject);
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
