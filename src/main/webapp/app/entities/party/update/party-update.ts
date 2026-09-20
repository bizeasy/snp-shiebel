import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { Gender } from 'app/entities/enumerations/gender.model';
import { IPartyType } from 'app/entities/party-type/party-type.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IParty } from '../party.model';
import { PartyService } from '../service/party.service';

import { PartyFormGroup, PartyFormService } from './party-form.service';
import { type BlobType } from 'app/shared/jhipster/data-utils';
import { PartyTypeService } from 'app/entities/party-type/service/party-type.service';
import { IStatus } from 'app/entities/status/status.model';
import { StatusService } from 'app/entities/status/service/status.service';
import { IPostalAddress } from 'app/entities/postal-address/postal-address.model';
import { PostalAddressService } from 'app/entities/postal-address/service/postal-address.service';

@Component({
  selector: 'sys-party-update',
  templateUrl: './party-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class PartyUpdate implements OnInit {
  readonly isSaving = signal(false);
  party: IParty | null = null;
  genderValues = Object.keys(Gender);

  usersSharedCollection = signal<IUser[]>([]);
  partyTypesSharedCollection = signal<IPartyType[]>([]);
  statusesSharedCollection = signal<IStatus[]>([]);
  postalAddressesSharedCollection = signal<IPostalAddress[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected partyService = inject(PartyService);
  protected partyFormService = inject(PartyFormService);
  protected userService = inject(UserService);
  protected partyTypeService = inject(PartyTypeService);
  protected statusService = inject(StatusService);
  protected postalAddressService = inject(PostalAddressService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PartyFormGroup = this.partyFormService.createPartyFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  comparePartyType = (o1: IPartyType | null, o2: IPartyType | null): boolean => this.partyTypeService.comparePartyType(o1, o2);

  compareStatus = (o1: IStatus | null, o2: IStatus | null): boolean => this.statusService.compareStatus(o1, o2);

  comparePostalAddress = (o1: IPostalAddress | null, o2: IPostalAddress | null): boolean =>
    this.postalAddressService.comparePostalAddress(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ party }) => {
      this.party = party;
      if (party) {
        this.updateForm(party);
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
    const party = this.partyFormService.getParty(this.editForm);
    if (party.id === null) {
      this.subscribeToSaveResponse(this.partyService.create(party));
    } else {
      this.subscribeToSaveResponse(this.partyService.update(party));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IParty | null>): void {
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

  protected updateForm(party: IParty): void {
    this.party = party;
    this.partyFormService.resetForm(this.editForm, party);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, party.user));
    this.partyTypesSharedCollection.update(partyTypes =>
      this.partyTypeService.addPartyTypeToCollectionIfMissing<IPartyType>(partyTypes, party.partyType),
    );
    this.statusesSharedCollection.update(statuses => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, party.status));
    this.postalAddressesSharedCollection.update(postalAddresses =>
      this.postalAddressService.addPostalAddressToCollectionIfMissing<IPostalAddress>(postalAddresses, party.address),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.party?.user)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));

    this.partyTypeService
      .query()
      .pipe(map((res: HttpResponse<IPartyType[]>) => res.body ?? []))
      .pipe(
        map((partyTypes: IPartyType[]) =>
          this.partyTypeService.addPartyTypeToCollectionIfMissing<IPartyType>(partyTypes, this.party?.partyType),
        ),
      )
      .subscribe((partyTypes: IPartyType[]) => this.partyTypesSharedCollection.set(partyTypes));

    this.statusService
      .query()
      .pipe(map((res: HttpResponse<IStatus[]>) => res.body ?? []))
      .pipe(map((statuses: IStatus[]) => this.statusService.addStatusToCollectionIfMissing<IStatus>(statuses, this.party?.status)))
      .subscribe((statuses: IStatus[]) => this.statusesSharedCollection.set(statuses));

    this.postalAddressService
      .query()
      .pipe(map((res: HttpResponse<IPostalAddress[]>) => res.body ?? []))
      .pipe(
        map((postalAddresses: IPostalAddress[]) =>
          this.postalAddressService.addPostalAddressToCollectionIfMissing<IPostalAddress>(postalAddresses, this.party?.address),
        ),
      )
      .subscribe((postalAddresses: IPostalAddress[]) => this.postalAddressesSharedCollection.set(postalAddresses));
  }
}
