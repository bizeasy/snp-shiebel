import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IInvoiceItemType } from 'app/entities/invoice-item-type/invoice-item-type.model';
import { InvoiceItemTypeService } from 'app/entities/invoice-item-type/service/invoice-item-type.service';
import { IInvoiceType } from 'app/entities/invoice-type/invoice-type.model';
import { InvoiceTypeService } from 'app/entities/invoice-type/service/invoice-type.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInvoiceItemTypeMap } from '../invoice-item-type-map.model';
import { InvoiceItemTypeMapService } from '../service/invoice-item-type-map.service';

import { InvoiceItemTypeMapFormGroup, InvoiceItemTypeMapFormService } from './invoice-item-type-map-form.service';

@Component({
  selector: 'sys-invoice-item-type-map-update',
  templateUrl: './invoice-item-type-map-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class InvoiceItemTypeMapUpdate implements OnInit {
  readonly isSaving = signal(false);
  invoiceItemTypeMap: IInvoiceItemTypeMap | null = null;

  invoiceTypesSharedCollection = signal<IInvoiceType[]>([]);
  invoiceItemTypesSharedCollection = signal<IInvoiceItemType[]>([]);

  protected invoiceItemTypeMapService = inject(InvoiceItemTypeMapService);
  protected invoiceItemTypeMapFormService = inject(InvoiceItemTypeMapFormService);
  protected invoiceTypeService = inject(InvoiceTypeService);
  protected invoiceItemTypeService = inject(InvoiceItemTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InvoiceItemTypeMapFormGroup = this.invoiceItemTypeMapFormService.createInvoiceItemTypeMapFormGroup();

  compareInvoiceType = (o1: IInvoiceType | null, o2: IInvoiceType | null): boolean => this.invoiceTypeService.compareInvoiceType(o1, o2);

  compareInvoiceItemType = (o1: IInvoiceItemType | null, o2: IInvoiceItemType | null): boolean =>
    this.invoiceItemTypeService.compareInvoiceItemType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceItemTypeMap }) => {
      this.invoiceItemTypeMap = invoiceItemTypeMap;
      if (invoiceItemTypeMap) {
        this.updateForm(invoiceItemTypeMap);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const invoiceItemTypeMap = this.invoiceItemTypeMapFormService.getInvoiceItemTypeMap(this.editForm);
    if (invoiceItemTypeMap.id === null) {
      this.subscribeToSaveResponse(this.invoiceItemTypeMapService.create(invoiceItemTypeMap));
    } else {
      this.subscribeToSaveResponse(this.invoiceItemTypeMapService.update(invoiceItemTypeMap));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInvoiceItemTypeMap | null>): void {
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

  protected updateForm(invoiceItemTypeMap: IInvoiceItemTypeMap): void {
    this.invoiceItemTypeMap = invoiceItemTypeMap;
    this.invoiceItemTypeMapFormService.resetForm(this.editForm, invoiceItemTypeMap);

    this.invoiceTypesSharedCollection.update(invoiceTypes =>
      this.invoiceTypeService.addInvoiceTypeToCollectionIfMissing<IInvoiceType>(invoiceTypes, invoiceItemTypeMap.invoiceType),
    );
    this.invoiceItemTypesSharedCollection.update(invoiceItemTypes =>
      this.invoiceItemTypeService.addInvoiceItemTypeToCollectionIfMissing<IInvoiceItemType>(
        invoiceItemTypes,
        invoiceItemTypeMap.invoiceItemType,
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.invoiceTypeService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceType[]>) => res.body ?? []))
      .pipe(
        map((invoiceTypes: IInvoiceType[]) =>
          this.invoiceTypeService.addInvoiceTypeToCollectionIfMissing<IInvoiceType>(invoiceTypes, this.invoiceItemTypeMap?.invoiceType),
        ),
      )
      .subscribe((invoiceTypes: IInvoiceType[]) => this.invoiceTypesSharedCollection.set(invoiceTypes));

    this.invoiceItemTypeService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItemType[]>) => res.body ?? []))
      .pipe(
        map((invoiceItemTypes: IInvoiceItemType[]) =>
          this.invoiceItemTypeService.addInvoiceItemTypeToCollectionIfMissing<IInvoiceItemType>(
            invoiceItemTypes,
            this.invoiceItemTypeMap?.invoiceItemType,
          ),
        ),
      )
      .subscribe((invoiceItemTypes: IInvoiceItemType[]) => this.invoiceItemTypesSharedCollection.set(invoiceItemTypes));
  }
}
