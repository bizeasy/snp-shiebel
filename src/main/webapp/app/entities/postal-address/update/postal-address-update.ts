import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IContactMech } from 'app/entities/contact-mech/contact-mech.model';
import { ContactMechService } from 'app/entities/contact-mech/service/contact-mech.service';
import { IGeo } from 'app/entities/geo/geo.model';
import { GeoService } from 'app/entities/geo/service/geo.service';
import { IGeoPoint } from 'app/entities/geo-point/geo-point.model';
import { GeoPointService } from 'app/entities/geo-point/service/geo-point.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPostalAddress } from '../postal-address.model';
import { PostalAddressService } from '../service/postal-address.service';

import { PostalAddressFormGroup, PostalAddressFormService } from './postal-address-form.service';

@Component({
  selector: 'sys-postal-address-update',
  templateUrl: './postal-address-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PostalAddressUpdate implements OnInit {
  readonly isSaving = signal(false);
  postalAddress: IPostalAddress | null = null;

  contactMechesSharedCollection = signal<IContactMech[]>([]);
  geosSharedCollection = signal<IGeo[]>([]);
  geoPointsSharedCollection = signal<IGeoPoint[]>([]);

  protected postalAddressService = inject(PostalAddressService);
  protected postalAddressFormService = inject(PostalAddressFormService);
  protected contactMechService = inject(ContactMechService);
  protected geoService = inject(GeoService);
  protected geoPointService = inject(GeoPointService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PostalAddressFormGroup = this.postalAddressFormService.createPostalAddressFormGroup();

  compareContactMech = (o1: IContactMech | null, o2: IContactMech | null): boolean => this.contactMechService.compareContactMech(o1, o2);

  compareGeo = (o1: IGeo | null, o2: IGeo | null): boolean => this.geoService.compareGeo(o1, o2);

  compareGeoPoint = (o1: IGeoPoint | null, o2: IGeoPoint | null): boolean => this.geoPointService.compareGeoPoint(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ postalAddress }) => {
      this.postalAddress = postalAddress;
      if (postalAddress) {
        this.updateForm(postalAddress);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const postalAddress = this.postalAddressFormService.getPostalAddress(this.editForm);
    if (postalAddress.id === null) {
      this.subscribeToSaveResponse(this.postalAddressService.create(postalAddress));
    } else {
      this.subscribeToSaveResponse(this.postalAddressService.update(postalAddress));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPostalAddress | null>): void {
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

  protected updateForm(postalAddress: IPostalAddress): void {
    this.postalAddress = postalAddress;
    this.postalAddressFormService.resetForm(this.editForm, postalAddress);

    this.contactMechesSharedCollection.update(contactMeches =>
      this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, postalAddress.contactMech),
    );
    this.geosSharedCollection.update(geos =>
      this.geoService.addGeoToCollectionIfMissing<IGeo>(geos, postalAddress.state, postalAddress.pincode, postalAddress.country),
    );
    this.geoPointsSharedCollection.update(geoPoints =>
      this.geoPointService.addGeoPointToCollectionIfMissing<IGeoPoint>(geoPoints, postalAddress.geoPoint),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactMechService
      .query()
      .pipe(map((res: HttpResponse<IContactMech[]>) => res.body ?? []))
      .pipe(
        map((contactMeches: IContactMech[]) =>
          this.contactMechService.addContactMechToCollectionIfMissing<IContactMech>(contactMeches, this.postalAddress?.contactMech),
        ),
      )
      .subscribe((contactMeches: IContactMech[]) => this.contactMechesSharedCollection.set(contactMeches));

    this.geoService
      .query()
      .pipe(map((res: HttpResponse<IGeo[]>) => res.body ?? []))
      .pipe(
        map((geos: IGeo[]) =>
          this.geoService.addGeoToCollectionIfMissing<IGeo>(
            geos,
            this.postalAddress?.state,
            this.postalAddress?.pincode,
            this.postalAddress?.country,
          ),
        ),
      )
      .subscribe((geos: IGeo[]) => this.geosSharedCollection.set(geos));

    this.geoPointService
      .query()
      .pipe(map((res: HttpResponse<IGeoPoint[]>) => res.body ?? []))
      .pipe(
        map((geoPoints: IGeoPoint[]) =>
          this.geoPointService.addGeoPointToCollectionIfMissing<IGeoPoint>(geoPoints, this.postalAddress?.geoPoint),
        ),
      )
      .subscribe((geoPoints: IGeoPoint[]) => this.geoPointsSharedCollection.set(geoPoints));
  }
}
