import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IProductFacility } from 'app/entities/product-facility/product-facility.model';
import { ProductFacilityService } from 'app/entities/product-facility/service/product-facility.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IProductFacilityUser } from '../product-facility-user.model';
import { ProductFacilityUserService } from '../service/product-facility-user.service';

import { ProductFacilityUserFormGroup, ProductFacilityUserFormService } from './product-facility-user-form.service';

@Component({
  selector: 'sys-product-facility-user-update',
  templateUrl: './product-facility-user-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ProductFacilityUserUpdate implements OnInit {
  readonly isSaving = signal(false);
  productFacilityUser: IProductFacilityUser | null = null;

  productFacilitiesSharedCollection = signal<IProductFacility[]>([]);
  partiesSharedCollection = signal<IParty[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);

  protected productFacilityUserService = inject(ProductFacilityUserService);
  protected productFacilityUserFormService = inject(ProductFacilityUserFormService);
  protected productFacilityService = inject(ProductFacilityService);
  protected partyService = inject(PartyService);
  protected statusService = inject(StatusService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductFacilityUserFormGroup = this.productFacilityUserFormService.createProductFacilityUserFormGroup();

  compareProductFacility = (o1: IProductFacility | null, o2: IProductFacility | null): boolean =>
    this.productFacilityService.compareProductFacility(o1, o2);

  compareParty = (o1: IParty | null, o2: IParty | null): boolean => this.partyService.compareParty(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productFacilityUser }) => {
      this.productFacilityUser = productFacilityUser;
      if (productFacilityUser) {
        this.updateForm(productFacilityUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const productFacilityUser = this.productFacilityUserFormService.getProductFacilityUser(this.editForm);
    if (productFacilityUser.id === null) {
      this.subscribeToSaveResponse(this.productFacilityUserService.create(productFacilityUser));
    } else {
      this.subscribeToSaveResponse(this.productFacilityUserService.update(productFacilityUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IProductFacilityUser | null>): void {
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

  protected updateForm(productFacilityUser: IProductFacilityUser): void {
    this.productFacilityUser = productFacilityUser;
    this.productFacilityUserFormService.resetForm(this.editForm, productFacilityUser);

    this.productFacilitiesSharedCollection.update(productFacilities =>
      this.productFacilityService.addProductFacilityToCollectionIfMissing<IProductFacility>(
        productFacilities,
        productFacilityUser.productFacility,
      ),
    );
    this.partiesSharedCollection.update(parties =>
      this.partyService.addPartyToCollectionIfMissing<IParty>(parties, productFacilityUser.party),
    );
    this.statusesSharedCollection.update(statuses =>
      this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, productFacilityUser.status),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productFacilityService
      .query()
      .pipe(map((res: HttpResponse<IProductFacility[]>) => res.body ?? []))
      .pipe(
        map((productFacilities: IProductFacility[]) =>
          this.productFacilityService.addProductFacilityToCollectionIfMissing<IProductFacility>(
            productFacilities,
            this.productFacilityUser?.productFacility,
          ),
        ),
      )
      .subscribe((productFacilities: IProductFacility[]) => this.productFacilitiesSharedCollection.set(productFacilities));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing<IParty>(parties, this.productFacilityUser?.party)))
      .subscribe((parties: IParty[]) => this.partiesSharedCollection.set(parties));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(
        map((statuses: IStatus[]) =>
          this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.productFacilityUser?.status),
        ),
      )
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));
  }
}
