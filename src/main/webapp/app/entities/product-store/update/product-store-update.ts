import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { TranslateDirective } from 'app/shared/language';
import { IProductStore } from '../product-store.model';
import { ProductStoreService } from '../service/product-store.service';

import { ProductStoreFormGroup, ProductStoreFormService } from './product-store-form.service';

@Component({
  selector: 'sys-product-store-update',
  templateUrl: './product-store-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductStoreUpdate implements OnInit {
  readonly isSaving = signal(false);
  productStore: IProductStore | null = null;

  partiesSharedCollection = signal<IParty[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected productStoreService = inject(ProductStoreService);
  protected productStoreFormService = inject(ProductStoreFormService);
  protected partyService = inject(PartyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductStoreFormGroup = this.productStoreFormService.createProductStoreFormGroup();

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productStore }) => {
      this.productStore = productStore;
      if (productStore) {
        this.updateForm(productStore);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined, blobType?: BlobType): void {
    this.dataUtils.openFile(base64String, contentType, blobType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertErrorModel>('snpApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productStore = this.productStoreFormService.getProductStore(this.editForm);
    if (productStore.id === null) {
      this.subscribeToSaveResponse(this.productStoreService.create(productStore));
    } else {
      this.subscribeToSaveResponse(this.productStoreService.update(productStore));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductStore | null>): void {
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

  protected updateForm(productStore: IProductStore): void {
    this.productStore = productStore;
    this.productStoreFormService.resetForm(this.editForm, productStore);

    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, productStore.owner, productStore.admin),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.productStore?.owner, this.productStore?.admin),
        ),
      )
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));
  }
}
