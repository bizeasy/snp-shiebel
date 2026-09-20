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
import { ProductFeatureCategoryDeleteDialog } from '../delete/product-feature-category-delete-dialog';
import { IProductFeatureCategory } from '../product-feature-category.model';
import { ProductFeatureCategoryService } from '../service/product-feature-category.service';

@Component({
  selector: 'sys-product-feature-category',
  templateUrl: './product-feature-category.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, TranslateDirective],
})
export class ProductFeatureCategory {
  readonly productFeatureCategories = signal<IProductFeatureCategory[]>([]);

  sortState = sortStateSignal({});

  readonly router = inject(Router);
  protected readonly productFeatureCategoryService = inject(ProductFeatureCategoryService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.productFeatureCategoryService.productFeatureCategoriesResource.isLoading;
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
      this.productFeatureCategories.set(
        this.fillComponentAttributesFromResponseBody([...this.productFeatureCategoryService.productFeatureCategories()]),
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

  trackId = (item: IProductFeatureCategory): number => this.productFeatureCategoryService.getProductFeatureCategoryIdentifier(item);

  delete(productFeatureCategory: IProductFeatureCategory): void {
    const modalRef = this.modalService.open(ProductFeatureCategoryDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productFeatureCategory = productFeatureCategory;
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

  protected refineData(data: IProductFeatureCategory[]): IProductFeatureCategory[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IProductFeatureCategory[]): IProductFeatureCategory[] {
    return this.refineData(data);
  }

  protected queryBackend(): void {
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.productFeatureCategoryService.productFeatureCategoriesParams.set(queryObject);
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
