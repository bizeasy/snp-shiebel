import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPartyType } from 'app/entities/party-type/party-type.model';
import { PartyTypeService } from 'app/entities/party-type/service/party-type.service';
import { IPostalAddress } from 'app/entities/postal-address/postal-address.model';
import { PostalAddressService } from 'app/entities/postal-address/service/postal-address.service';
import { StatusService } from 'app/entities/status/service/status.service';
import { IStatus } from 'app/entities/status/status.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IParty } from '../party.model';
import { PartyService } from '../service/party.service';

import { PartyFormService } from './party-form.service';
import { PartyUpdate } from './party-update';

describe('Party Management Update Component', () => {
  let comp: PartyUpdate;
  let fixture: ComponentFixture<PartyUpdate>;
  let activatedRoute: ActivatedRoute;
  let partyFormService: PartyFormService;
  let partyService: PartyService;
  let userService: UserService;
  let partyTypeService: PartyTypeService;
  let statusService: StatusService;
  let postalAddressService: PostalAddressService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PartyUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partyFormService = TestBed.inject(PartyFormService);
    partyService = TestBed.inject(PartyService);
    userService = TestBed.inject(UserService);
    partyTypeService = TestBed.inject(PartyTypeService);
    statusService = TestBed.inject(StatusService);
    postalAddressService = TestBed.inject(PostalAddressService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const party: IParty = { id: 17981 };
      const user: IUser = { id: 3944 };
      party.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ party });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PartyType query and add missing value', () => {
      const party: IParty = { id: 17981 };
      const partyType: IPartyType = { id: 3927 };
      party.partyType = partyType;

      const partyTypeCollection: IPartyType[] = [{ id: 3927 }];
      vi.spyOn(partyTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: partyTypeCollection })));
      const additionalPartyTypes = [partyType];
      const expectedCollection: IPartyType[] = [...additionalPartyTypes, ...partyTypeCollection];
      vi.spyOn(partyTypeService, 'addPartyTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ party });
      comp.ngOnInit();

      expect(partyTypeService.query).toHaveBeenCalled();
      expect(partyTypeService.addPartyTypeToCollectionIfMissing).toHaveBeenCalledWith(
        partyTypeCollection,
        ...additionalPartyTypes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partyTypesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Status query and add missing value', () => {
      const party: IParty = { id: 17981 };
      const status: IStatus = { id: 2927 };
      party.status = status;

      const statusCollection: IStatus[] = [{ id: 2927 }];
      vi.spyOn(statusService, 'query').mockReturnValue(of(new HttpResponse({ body: statusCollection })));
      const additionalStatuses = [status];
      const expectedCollection: IStatus[] = [...additionalStatuses, ...statusCollection];
      vi.spyOn(statusService, 'addStatusToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ party });
      comp.ngOnInit();

      expect(statusService.query).toHaveBeenCalled();
      expect(statusService.addStatusToCollectionIfMissing).toHaveBeenCalledWith(
        statusCollection,
        ...additionalStatuses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.statusesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PostalAddress query and add missing value', () => {
      const party: IParty = { id: 17981 };
      const address: IPostalAddress = { id: 25687 };
      party.address = address;

      const postalAddressCollection: IPostalAddress[] = [{ id: 25687 }];
      vi.spyOn(postalAddressService, 'query').mockReturnValue(of(new HttpResponse({ body: postalAddressCollection })));
      const additionalPostalAddresses = [address];
      const expectedCollection: IPostalAddress[] = [...additionalPostalAddresses, ...postalAddressCollection];
      vi.spyOn(postalAddressService, 'addPostalAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ party });
      comp.ngOnInit();

      expect(postalAddressService.query).toHaveBeenCalled();
      expect(postalAddressService.addPostalAddressToCollectionIfMissing).toHaveBeenCalledWith(
        postalAddressCollection,
        ...additionalPostalAddresses.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.postalAddressesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const party: IParty = { id: 17981 };
      const user: IUser = { id: 3944 };
      party.user = user;
      const partyType: IPartyType = { id: 3927 };
      party.partyType = partyType;
      const status: IStatus = { id: 2927 };
      party.status = status;
      const address: IPostalAddress = { id: 25687 };
      party.address = address;

      activatedRoute.data = of({ party });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(user);
      expect(comp.partyTypesSharedCollection()).toContainEqual(partyType);
      expect(comp.statusesSharedCollection()).toContainEqual(status);
      expect(comp.postalAddressesSharedCollection()).toContainEqual(address);
      expect(comp.party).toEqual(party);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IParty>();
      const party = { id: 24527 };
      vi.spyOn(partyFormService, 'getParty').mockReturnValue(party);
      vi.spyOn(partyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ party });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(party);
      saveSubject.complete();

      // THEN
      expect(partyFormService.getParty).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partyService.update).toHaveBeenCalledWith(expect.objectContaining(party));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IParty>();
      const party = { id: 24527 };
      vi.spyOn(partyFormService, 'getParty').mockReturnValue({ id: null });
      vi.spyOn(partyService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ party: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(party);
      saveSubject.complete();

      // THEN
      expect(partyFormService.getParty).toHaveBeenCalled();
      expect(partyService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IParty>();
      const party = { id: 24527 };
      vi.spyOn(partyService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ party });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partyService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vi.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePartyType', () => {
      it('should forward to partyTypeService', () => {
        const entity = { id: 3927 };
        const entity2 = { id: 27525 };
        vi.spyOn(partyTypeService, 'comparePartyType');
        comp.comparePartyType(entity, entity2);
        expect(partyTypeService.comparePartyType).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareStatus', () => {
      it('should forward to statusService', () => {
        const entity = { id: 2927 };
        const entity2 = { id: 22202 };
        vi.spyOn(statusService, 'compareStatus');
        comp.compareStatus(entity, entity2);
        expect(statusService.compareStatus).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePostalAddress', () => {
      it('should forward to postalAddressService', () => {
        const entity = { id: 25687 };
        const entity2 = { id: 32221 };
        vi.spyOn(postalAddressService, 'comparePostalAddress');
        comp.comparePostalAddress(entity, entity2);
        expect(postalAddressService.comparePostalAddress).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
