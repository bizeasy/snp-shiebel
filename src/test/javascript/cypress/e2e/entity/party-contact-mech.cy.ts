import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('PartyContactMech e2e test', () => {
  const partyContactMechPageUrl = '/party-contact-mech';
  let username: string;
  let password: string;
  const partyContactMechSample = { fromDate: '2023-12-14T03:36:57.644Z' };

  let partyContactMech;
  let party;
  let contactMech;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'vet minor rapidly',
        organisationShortName: 'elegantly oof',
        salutation: 'bah aside',
        firstName: 'Shayne',
        middleName: 'ack between braid',
        lastName: 'Reynolds',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'brr sport down',
        primaryEmail: 'content pish though',
        isTemporaryPassword: false,
        logoImageUrl: 'where',
        profileImageUrl: 'boo haircut',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/contact-meches',
      body: { infoString: 'when' },
    }).then(({ body }) => {
      contactMech = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-contact-meches+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-contact-meches').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-contact-meches/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/contact-meches', {
      statusCode: 200,
      body: [contactMech],
    });

    cy.intercept('GET', '/api/contact-mech-purposes', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (partyContactMech) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-contact-meches/${partyContactMech.id}`,
      }).then(() => {
        partyContactMech = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
    if (contactMech) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contact-meches/${contactMech.id}`,
      }).then(() => {
        contactMech = undefined;
      });
    }
  });

  it('PartyContactMeches menu should load PartyContactMeches page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-contact-mech');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyContactMech').should('exist');
    cy.location('pathname').should('eq', partyContactMechPageUrl);
  });

  describe('PartyContactMech page', () => {
    it('should have translated page title', () => {
      cy.visit(partyContactMechPageUrl);
      cy.getEntityHeading('PartyContactMech').should('not.contain', 'snpApp.partyContactMech.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyContactMechPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyContactMech page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyContactMechPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyContactMechPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-contact-meches',
          body: {
            ...partyContactMechSample,
            party,
            contactMech,
          },
        }).then(({ body }) => {
          partyContactMech = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-contact-meches+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/party-contact-meches?page=0&size=20>; rel="last",<http://localhost/api/party-contact-meches?page=0&size=20>; rel="first"',
              },
              body: [partyContactMech],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyContactMechPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyContactMech page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyContactMech');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyContactMechPageUrl);
      });

      it('edit button click should load edit PartyContactMech page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyContactMech');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyContactMechPageUrl);
      });

      it('edit button click should load edit PartyContactMech page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyContactMech');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyContactMechPageUrl);
      });

      it('last delete button click should delete instance of PartyContactMech', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyContactMech').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyContactMechPageUrl);

        partyContactMech = undefined;
      });
    });
  });

  describe('new PartyContactMech page', () => {
    beforeEach(() => {
      cy.visit(partyContactMechPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyContactMech');
    });

    it('should create an instance of PartyContactMech', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T11:24');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T11:24');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T00:31');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T00:31');

      cy.get(`[data-cy="party"]`).select(1);
      cy.get(`[data-cy="contactMech"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyContactMech = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyContactMechPageUrl);
    });
  });
});
