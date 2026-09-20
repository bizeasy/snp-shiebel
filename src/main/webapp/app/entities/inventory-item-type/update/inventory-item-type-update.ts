import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventoryItemType } from '../inventory-item-type.model';
import { InventoryItemTypeService } from '../service/inventory-item-type.service';

import { InventoryItemTypeFormGroup, InventoryItemTypeFormService } from './inventory-item-type-form.service';

@Component({
  selector: 'sys-inventory-item-type-update',
  templateUrl: './inventory-item-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InventoryItemTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  inventoryItemType: IInventoryItemType | null = null;

  protected inventoryItemTypeService = inject(InventoryItemTypeService);
  protected inventoryItemTypeFormService = inject(InventoryItemTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventoryItemTypeFormGroup = this.inventoryItemTypeFormService.createInventoryItemTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventoryItemType }) => {
      this.inventoryItemType = inventoryItemType;
      if (inventoryItemType) {
        this.updateForm(inventoryItemType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const inventoryItemType = this.inventoryItemTypeFormService.getInventoryItemType(this.editForm);
    if (inventoryItemType.id === null) {
      this.subscribeToSaveResponse(this.inventoryItemTypeService.create(inventoryItemType));
    } else {
      this.subscribeToSaveResponse(this.inventoryItemTypeService.update(inventoryItemType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInventoryItemType | null>): void {
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

  protected updateForm(inventoryItemType: IInventoryItemType): void {
    this.inventoryItemType = inventoryItemType;
    this.inventoryItemTypeFormService.resetForm(this.editForm, inventoryItemType);
  }
}
