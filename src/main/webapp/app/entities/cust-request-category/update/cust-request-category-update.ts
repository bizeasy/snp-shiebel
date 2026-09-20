import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ICustRequestType } from 'app/entities/cust-request-type/cust-request-type.model';
import { CustRequestTypeService } from 'app/entities/cust-request-type/service/cust-request-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICustRequestCategory } from '../cust-request-category.model';
import { CustRequestCategoryService } from '../service/cust-request-category.service';

import { CustRequestCategoryFormGroup, CustRequestCategoryFormService } from './cust-request-category-form.service';

@Component({
  selector: 'sys-cust-request-category-update',
  templateUrl: './cust-request-category-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class CustRequestCategoryUpdate implements OnInit {
  readonly isSaving = signal(false);
  custRequestCategory: ICustRequestCategory | null = null;

  custRequestTypesSharedCollection = signal<ICustRequestType[]>([]);

  protected custRequestCategoryService = inject(CustRequestCategoryService);
  protected custRequestCategoryFormService = inject(CustRequestCategoryFormService);
  protected custRequestTypeService = inject(CustRequestTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustRequestCategoryFormGroup = this.custRequestCategoryFormService.createCustRequestCategoryFormGroup();

  compareCustRequestType = (o1: ICustRequestType | null, o2: ICustRequestType | null): boolean =>
    this.custRequestTypeService.compareCustRequestType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ custRequestCategory }) => {
      this.custRequestCategory = custRequestCategory;
      if (custRequestCategory) {
        this.updateForm(custRequestCategory);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const custRequestCategory = this.custRequestCategoryFormService.getCustRequestCategory(this.editForm);
    if (custRequestCategory.id === null) {
      this.subscribeToSaveResponse(this.custRequestCategoryService.create(custRequestCategory));
    } else {
      this.subscribeToSaveResponse(this.custRequestCategoryService.update(custRequestCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ICustRequestCategory | null>): void {
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

  protected updateForm(custRequestCategory: ICustRequestCategory): void {
    this.custRequestCategory = custRequestCategory;
    this.custRequestCategoryFormService.resetForm(this.editForm, custRequestCategory);

    this.custRequestTypesSharedCollection.update(custRequestTypes =>
      this.custRequestTypeService.addCustRequestTypeToCollectionIfMissing<ICustRequestType>(
        custRequestTypes,
        custRequestCategory.custRequestType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.custRequestTypeService
      .query()
      .pipe(map((res: HttpResponse<ICustRequestType[]>) => res.body ?? []))
      .pipe(
        map((custRequestTypes: ICustRequestType[]) =>
          this.custRequestTypeService.addCustRequestTypeToCollectionIfMissing<ICustRequestType>(
            custRequestTypes,
            this.custRequestCategory?.custRequestType,
          ),
        ),
      )
      .subscribe((custRequestTypes: ICustRequestType[]) => this.custRequestTypesSharedCollection.set(custRequestTypes));
  }
}
