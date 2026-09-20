import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { UomService } from 'app/entities/uom/service/uom.service';
import { IUom } from 'app/entities/uom/uom.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { UomConversionService } from '../service/uom-conversion.service';
import { IUomConversion } from '../uom-conversion.model';

import { UomConversionFormGroup, UomConversionFormService } from './uom-conversion-form.service';

@Component({
  selector: 'sys-uom-conversion-update',
  templateUrl: './uom-conversion-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class UomConversionUpdate implements OnInit {
  readonly isSaving = signal(false);
  uomConversion: IUomConversion | null = null;

  uomsSharedCollection = signal<IUom[]>([]);

  protected uomConversionService = inject(UomConversionService);
  protected uomConversionFormService = inject(UomConversionFormService);
  protected uomService = inject(UomService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UomConversionFormGroup = this.uomConversionFormService.createUomConversionFormGroup();

  compareUom = (o1: IUom | null, o2: IUom | null): boolean => this.uomService.compareUom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ uomConversion }) => {
      this.uomConversion = uomConversion;
      if (uomConversion) {
        this.updateForm(uomConversion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const uomConversion = this.uomConversionFormService.getUomConversion(this.editForm);
    if (uomConversion.id === null) {
      this.subscribeToSaveResponse(this.uomConversionService.create(uomConversion));
    } else {
      this.subscribeToSaveResponse(this.uomConversionService.update(uomConversion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IUomConversion | null>): void {
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

  protected updateForm(uomConversion: IUomConversion): void {
    this.uomConversion = uomConversion;
    this.uomConversionFormService.resetForm(this.editForm, uomConversion);

    this.uomsSharedCollection.update(uoms =>
      this.uomService.addUomToCollectionIfMissing<IUom>(uoms, uomConversion.fromUom, uomConversion.toUom),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.uomService
      .query()
      .pipe(map((res: HttpResponse<IUom[]>) => res.body ?? []))
      .pipe(
        map((uoms: IUom[]) =>
          this.uomService.addUomToCollectionIfMissing<IUom>(uoms, this.uomConversion?.fromUom, this.uomConversion?.toUom),
        ),
      )
      .subscribe((uoms: IUom[]) => this.uomsSharedCollection.set(uoms));
  }
}
