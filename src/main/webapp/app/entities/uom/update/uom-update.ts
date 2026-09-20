import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { UomTypeService } from 'app/entities/uom-type/service/uom-type.service';
import { IUomType } from 'app/entities/uom-type/uom-type.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { UomService } from '../service/uom.service';
import { IUom } from '../uom.model';

import { UomFormGroup, UomFormService } from './uom-form.service';

@Component({
  selector: 'sys-uom-update',
  templateUrl: './uom-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class UomUpdate implements OnInit {
  readonly isSaving = signal(false);
  uom: IUom | null = null;

  uomTypesSharedCollection = signal<IUomType[]>([]);

  protected uomService = inject(UomService);
  protected uomFormService = inject(UomFormService);
  protected uomTypeService = inject(UomTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UomFormGroup = this.uomFormService.createUomFormGroup();

  compareUomType = (o1: IUomType | null, o2: IUomType | null): boolean => this.uomTypeService.compareUomType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uom }) => {
      this.uom = uom;
      if (uom) {
        this.updateForm(uom);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const uom = this.uomFormService.getUom(this.editForm);
    if (uom.id === null) {
      this.subscribeToSaveResponse(this.uomService.create(uom));
    } else {
      this.subscribeToSaveResponse(this.uomService.update(uom));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IUom | null>): void {
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

  protected updateForm(uom: IUom): void {
    this.uom = uom;
    this.uomFormService.resetForm(this.editForm, uom);

    this.uomTypesSharedCollection.update(uomTypes => this.uomTypeService.addUomTypeToCollectionIfMissing<IUomType>(uomTypes, uom.uomType));
  }

  protected loadRelationshipsOptions(): void {
    this.uomTypeService
      .query()
      .pipe(map((res: HttpResponse<IUomType[]>) => res.body ?? []))
      .pipe(map((uomTypes: IUomType[]) => this.uomTypeService.addUomTypeToCollectionIfMissing<IUomType>(uomTypes, this.uom?.uomType)))
      .subscribe((uomTypes: IUomType[]) => this.uomTypesSharedCollection.set(uomTypes));
  }
}
