import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInventoryItem } from 'app/entities/inventory-item/inventory-item.model';
import { InventoryItemService } from 'app/entities/inventory-item/service/inventory-item.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItemVariance } from '../inventory-item-variance.model';
import { InventoryItemVarianceService } from '../service/inventory-item-variance.service';

import { InventoryItemVarianceFormGroup, InventoryItemVarianceFormService } from './inventory-item-variance-form.service';

@Component({
  selector: 'sys-inventory-item-variance-update',
  templateUrl: './inventory-item-variance-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InventoryItemVarianceUpdate implements OnInit {
  readonly isSaving = signal(false);
  inventoryItemVariance: IInventoryItemVariance | null = null;

  inventoryItemsSharedCollection = signal<IInventoryItem[]>([]);

  protected inventoryItemVarianceService = inject(InventoryItemVarianceService);
  protected inventoryItemVarianceFormService = inject(InventoryItemVarianceFormService);
  protected inventoryItemService = inject(InventoryItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryItemVarianceFormGroup = this.inventoryItemVarianceFormService.createInventoryItemVarianceFormGroup();

  compareInventoryItem = (o1: IInventoryItem | null, o2: IInventoryItem | null): boolean =>
    this.inventoryItemService.compareInventoryItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItemVariance }) => {
      this.inventoryItemVariance = inventoryItemVariance;
      if (inventoryItemVariance) {
        this.updateForm(inventoryItemVariance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const inventoryItemVariance = this.inventoryItemVarianceFormService.getInventoryItemVariance(this.editForm);
    if (inventoryItemVariance.id === null) {
      this.subscribeToSaveResponse(this.inventoryItemVarianceService.create(inventoryItemVariance));
    } else {
      this.subscribeToSaveResponse(this.inventoryItemVarianceService.update(inventoryItemVariance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInventoryItemVariance | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(inventoryItemVariance: IInventoryItemVariance): void {
    this.inventoryItemVariance = inventoryItemVariance;
    this.inventoryItemVarianceFormService.resetForm(this.editForm, inventoryItemVariance);

    this.inventoryItemsSharedCollection.update(inventoryItems =>
      this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(inventoryItems, inventoryItemVariance.inventoryItem),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.inventoryItemService
      .query()
      .pipe(map((res: HttpResponse<IInventoryItem[]>) => res.body ?? []))
      .pipe(
        map((inventoryItems: IInventoryItem[]) =>
          this.inventoryItemService.addInventoryItemToCollectionIfMissing<IInventoryItem>(
            inventoryItems,
            this.inventoryItemVariance?.inventoryItem,
          ),
        ),
      )
      .subscribe((inventoryItems: IInventoryItem[]) => this.inventoryItemsSharedCollection.set(inventoryItems));
  }
}
