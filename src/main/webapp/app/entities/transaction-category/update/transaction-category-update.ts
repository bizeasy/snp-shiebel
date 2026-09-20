import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TransactionCategoryService } from '../service/transaction-category.service';
import { ITransactionCategory } from '../transaction-category.model';

import { TransactionCategoryFormGroup, TransactionCategoryFormService } from './transaction-category-form.service';

@Component({
  selector: 'sys-transaction-category-update',
  templateUrl: './transaction-category-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TransactionCategoryUpdate implements OnInit {
  readonly isSaving = signal(false);
  transactionCategory: ITransactionCategory | null = null;

  protected transactionCategoryService = inject(TransactionCategoryService);
  protected transactionCategoryFormService = inject(TransactionCategoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionCategoryFormGroup = this.transactionCategoryFormService.createTransactionCategoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionCategory }) => {
      this.transactionCategory = transactionCategory;
      if (transactionCategory) {
        this.updateForm(transactionCategory);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const transactionCategory = this.transactionCategoryFormService.getTransactionCategory(this.editForm);
    if (transactionCategory.id === null) {
      this.subscribeToSaveResponse(this.transactionCategoryService.create(transactionCategory));
    } else {
      this.subscribeToSaveResponse(this.transactionCategoryService.update(transactionCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITransactionCategory | null>): void {
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

  protected updateForm(transactionCategory: ITransactionCategory): void {
    this.transactionCategory = transactionCategory;
    this.transactionCategoryFormService.resetForm(this.editForm, transactionCategory);
  }
}
