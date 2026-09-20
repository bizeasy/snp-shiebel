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
import { OrderAdjustmentTypeDeleteDialog } from '../delete/order-adjustment-type-delete-dialog';
import { IOrderAdjustmentType } from '../order-adjustment-type.model';
import { OrderAdjustmentTypeService } from '../service/order-adjustment-type.service';

@Component({
  selector: 'sys-order-adjustment-type',
  templateUrl: './order-adjustment-type.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class OrderAdjustmentType {
  readonly orderAdjustmentTypes = signal<IOrderAdjustmentType[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly orderAdjustmentTypeService = inject(OrderAdjustmentTypeService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.orderAdjustmentTypeService.orderAdjustmentTypesResource.isLoading;
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
      this.orderAdjustmentTypes.set(
        this.fillComponentAttributesFromResponseBody([...this.orderAdjustmentTypeService.orderAdjustmentTypes()]),
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

  trackId = (item: IOrderAdjustmentType): number => this.orderAdjustmentTypeService.getOrderAdjustmentTypeIdentifier(item);

  delete(orderAdjustmentType: IOrderAdjustmentType): void {
    const modalRef = this.modalService.open(OrderAdjustmentTypeDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderAdjustmentType = orderAdjustmentType;
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

  protected refineData(data: IOrderAdjustmentType[]): IOrderAdjustmentType[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IOrderAdjustmentType[]): IOrderAdjustmentType[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.orderAdjustmentTypeService.orderAdjustmentTypesParams.set(queryObject);
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
