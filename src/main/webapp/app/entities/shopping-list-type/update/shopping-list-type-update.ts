import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ShoppingListTypeService } from '../service/shopping-list-type.service';
import { IShoppingListType } from '../shopping-list-type.model';

import { ShoppingListTypeFormGroup, ShoppingListTypeFormService } from './shopping-list-type-form.service';

@Component({
  selector: 'sys-shopping-list-type-update',
  templateUrl: './shopping-list-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ShoppingListTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  shoppingListType: IShoppingListType | null = null;

  protected shoppingListTypeService = inject(ShoppingListTypeService);
  protected shoppingListTypeFormService = inject(ShoppingListTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ShoppingListTypeFormGroup = this.shoppingListTypeFormService.createShoppingListTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shoppingListType }) => {
      this.shoppingListType = shoppingListType;
      if (shoppingListType) {
        this.updateForm(shoppingListType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const shoppingListType = this.shoppingListTypeFormService.getShoppingListType(this.editForm);
    if (shoppingListType.id === null) {
      this.subscribeToSaveResponse(this.shoppingListTypeService.create(shoppingListType));
    } else {
      this.subscribeToSaveResponse(this.shoppingListTypeService.update(shoppingListType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IShoppingListType | null>): void {
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

  protected updateForm(shoppingListType: IShoppingListType): void {
    this.shoppingListType = shoppingListType;
    this.shoppingListTypeFormService.resetForm(this.editForm, shoppingListType);
  }
}
