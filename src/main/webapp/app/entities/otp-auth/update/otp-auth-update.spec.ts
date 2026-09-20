import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IOtpAuth } from '../otp-auth.model';
import { OtpAuthService } from '../service/otp-auth.service';

import { OtpAuthFormService } from './otp-auth-form.service';
import { OtpAuthUpdate } from './otp-auth-update';

describe('OtpAuth Management Update Component', () => {
  let comp: OtpAuthUpdate;
  let fixture: ComponentFixture<OtpAuthUpdate>;
  let activatedRoute: ActivatedRoute;
  let otpAuthFormService: OtpAuthFormService;
  let otpAuthService: OtpAuthService;
  let partyService: PartyService;

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

    fixture = TestBed.createComponent(OtpAuthUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    otpAuthFormService = TestBed.inject(OtpAuthFormService);
    otpAuthService = TestBed.inject(OtpAuthService);
    partyService = TestBed.inject(PartyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Party query and add missing value', () => {
      const otpAuth: IOtpAuth = { id: 5252 };
      const party: IParty = { id: 24527 };
      otpAuth.party = party;

      const partyCollection: IParty[] = [{ id: 24527 }];
      vi.spyOn(partyService, 'query').mockReturnValue(of(new HttpResponse({ body: partyCollection })));
      const additionalParties = [party];
      const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
      vi.spyOn(partyService, 'addPartyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ otpAuth });
      comp.ngOnInit();

      expect(partyService.query).toHaveBeenCalled();
      expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(
        partyCollection,
        ...additionalParties.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.partiesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const otpAuth: IOtpAuth = { id: 5252 };
      const party: IParty = { id: 24527 };
      otpAuth.party = party;

      activatedRoute.data = of({ otpAuth });
      comp.ngOnInit();

      expect(comp.partiesSharedCollection()).toContainEqual(party);
      expect(comp.otpAuth).toEqual(otpAuth);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOtpAuth>();
      const otpAuth = { id: 23261 };
      vi.spyOn(otpAuthFormService, 'getOtpAuth').mockReturnValue(otpAuth);
      vi.spyOn(otpAuthService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ otpAuth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(otpAuth);
      saveSubject.complete();

      // THEN
      expect(otpAuthFormService.getOtpAuth).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(otpAuthService.update).toHaveBeenCalledWith(expect.objectContaining(otpAuth));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOtpAuth>();
      const otpAuth = { id: 23261 };
      vi.spyOn(otpAuthFormService, 'getOtpAuth').mockReturnValue({ id: null });
      vi.spyOn(otpAuthService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ otpAuth: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(otpAuth);
      saveSubject.complete();

      // THEN
      expect(otpAuthFormService.getOtpAuth).toHaveBeenCalled();
      expect(otpAuthService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOtpAuth>();
      const otpAuth = { id: 23261 };
      vi.spyOn(otpAuthService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ otpAuth });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(otpAuthService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParty', () => {
      it('should forward to partyService', () => {
        const entity = { id: 24527 };
        const entity2 = { id: 17981 };
        vi.spyOn(partyService, 'compareParty');
        comp.compareParty(entity, entity2);
        expect(partyService.compareParty).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
