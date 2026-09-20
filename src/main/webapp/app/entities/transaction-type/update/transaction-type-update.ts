import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { TransactionCategoryService } from 'app/entities/transaction-category/service/transaction-category.service';
import { ITransactionCategory } from 'app/entities/transaction-category/transaction-category.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TransactionTypeService } from '../service/transaction-type.service';
import { ITransactionType } from '../transaction-type.model';

import { TransactionTypeFormGroup, TransactionTypeFormService } from './transaction-type-form.service';

@Component({
  selector: 'sys-transaction-type-update',
  templateUrl: './transaction-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TransactionTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  transactionType: ITransactionType | null = null;

  transactionCategoriesSharedCollection = signal<ITransactionCategory[]>([]);

  protected transactionTypeService = inject(TransactionTypeService);
  protected transactionTypeFormService = inject(TransactionTypeFormService);
  protected transactionCategoryService = inject(TransactionCategoryService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransactionTypeFormGroup = this.transactionTypeFormService.createTransactionTypeFormGroup();

  compareTransactionCategory = (o1: ITransactionCategory | null, o2: ITransactionCategory | null): boolean =>
    this.transactionCategoryService.compareTransactionCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transactionType }) => {
      this.transactionType = transactionType;
      if (transactionType) {
        this.updateForm(transactionType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const transactionType = this.transactionTypeFormService.getTransactionType(this.editForm);
    if (transactionType.id === null) {
      this.subscribeToSaveResponse(this.transactionTypeService.create(transactionType));
    } else {
      this.subscribeToSaveResponse(this.transactionTypeService.update(transactionType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITransactionType | null>): void {
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

  protected updateForm(transactionType: ITransactionType): void {
    this.transactionType = transactionType;
    this.transactionTypeFormService.resetForm(this.editForm, transactionType);

    this.transactionCategoriesSharedCollection.update(transactionCategories =>
      this.transactionCategoryService.addTransactionCategoryToCollectionIfMissing<ITransactionCategory>(
        transactionCategories,
        transactionType.category,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transactionCategoryService
      .query()
      .pipe(map((res: HttpResponse<ITransactionCategory[]>) => res.body ?? []))
      .pipe(
        map((transactionCategories: ITransactionCategory[]) =>
          this.transactionCategoryService.addTransactionCategoryToCollectionIfMissing<ITransactionCategory>(
            transactionCategories,
            this.transactionType?.category,
          ),
        ),
      )
      .subscribe((transactionCategories: ITransactionCategory[]) => this.transactionCategoriesSharedCollection.set(transactionCategories));
  }
}
