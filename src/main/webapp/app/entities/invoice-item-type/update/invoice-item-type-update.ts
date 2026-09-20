import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceItemType } from '../invoice-item-type.model';
import { InvoiceItemTypeService } from '../service/invoice-item-type.service';

import { InvoiceItemTypeFormGroup, InvoiceItemTypeFormService } from './invoice-item-type-form.service';

@Component({
  selector: 'sys-invoice-item-type-update',
  templateUrl: './invoice-item-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InvoiceItemTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  invoiceItemType: IInvoiceItemType | null = null;

  protected invoiceItemTypeService = inject(InvoiceItemTypeService);
  protected invoiceItemTypeFormService = inject(InvoiceItemTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceItemTypeFormGroup = this.invoiceItemTypeFormService.createInvoiceItemTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceItemType }) => {
      this.invoiceItemType = invoiceItemType;
      if (invoiceItemType) {
        this.updateForm(invoiceItemType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const invoiceItemType = this.invoiceItemTypeFormService.getInvoiceItemType(this.editForm);
    if (invoiceItemType.id === null) {
      this.subscribeToSaveResponse(this.invoiceItemTypeService.create(invoiceItemType));
    } else {
      this.subscribeToSaveResponse(this.invoiceItemTypeService.update(invoiceItemType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInvoiceItemType | null>): void {
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

  protected updateForm(invoiceItemType: IInvoiceItemType): void {
    this.invoiceItemType = invoiceItemType;
    this.invoiceItemTypeFormService.resetForm(this.editForm, invoiceItemType);
  }
}
