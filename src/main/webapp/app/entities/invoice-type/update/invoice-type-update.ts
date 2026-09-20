import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceType } from '../invoice-type.model';
import { InvoiceTypeService } from '../service/invoice-type.service';

import { InvoiceTypeFormGroup, InvoiceTypeFormService } from './invoice-type-form.service';

@Component({
  selector: 'sys-invoice-type-update',
  templateUrl: './invoice-type-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InvoiceTypeUpdate implements OnInit {
  readonly isSaving = signal(false);
  invoiceType: IInvoiceType | null = null;

  protected invoiceTypeService = inject(InvoiceTypeService);
  protected invoiceTypeFormService = inject(InvoiceTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceTypeFormGroup = this.invoiceTypeFormService.createInvoiceTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceType }) => {
      this.invoiceType = invoiceType;
      if (invoiceType) {
        this.updateForm(invoiceType);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const invoiceType = this.invoiceTypeFormService.getInvoiceType(this.editForm);
    if (invoiceType.id === null) {
      this.subscribeToSaveResponse(this.invoiceTypeService.create(invoiceType));
    } else {
      this.subscribeToSaveResponse(this.invoiceTypeService.update(invoiceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInvoiceType | null>): void {
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

  protected updateForm(invoiceType: IInvoiceType): void {
    this.invoiceType = invoiceType;
    this.invoiceTypeFormService.resetForm(this.editForm, invoiceType);
  }
}
