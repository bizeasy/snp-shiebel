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

describe('PartyRelationship e2e test', () => {
  const partyRelationshipPageUrl = '/party-relationship';
  let username: string;
  let password: string;
  const partyRelationshipSample = { fromDate: '2023-12-14T18:10:04.683Z' };

  let partyRelationship;
  let party;

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
        isOrganisation: false,
        organisationName: 'edible',
        organisationShortName: 'boohoo highlight psst',
        salutation: 'silently',
        firstName: 'Cooper',
        middleName: 'bookcase the however',
        lastName: 'Boyle',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'design above',
        primaryEmail: 'when behind',
        isTemporaryPassword: true,
        logoImageUrl: 'fixed',
        profileImageUrl: 'black-and-white',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/party-relationships+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/party-relationships').as('postEntityRequest');
    cy.intercept('DELETE', '/api/party-relationships/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/party-relationship-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (partyRelationship) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/party-relationships/${partyRelationship.id}`,
      }).then(() => {
        partyRelationship = undefined;
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
  });

  it('PartyRelationships menu should load PartyRelationships page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('party-relationship');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PartyRelationship').should('exist');
    cy.location('pathname').should('eq', partyRelationshipPageUrl);
  });

  describe('PartyRelationship page', () => {
    it('should have translated page title', () => {
      cy.visit(partyRelationshipPageUrl);
      cy.getEntityHeading('PartyRelationship').should('not.contain', 'snpApp.partyRelationship.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(partyRelationshipPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PartyRelationship page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${partyRelationshipPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PartyRelationship');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/party-relationships',
          body: {
            ...partyRelationshipSample,
            partyFrom: party,
            partyTo: party,
          },
        }).then(({ body }) => {
          partyRelationship = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/party-relationships+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/party-relationships?page=0&size=20>; rel="last",<http://localhost/api/party-relationships?page=0&size=20>; rel="first"',
              },
              body: [partyRelationship],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(partyRelationshipPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PartyRelationship page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('partyRelationship');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipPageUrl);
      });

      it('edit button click should load edit PartyRelationship page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyRelationship');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipPageUrl);
      });

      it('edit button click should load edit PartyRelationship page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PartyRelationship');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipPageUrl);
      });

      it('last delete button click should delete instance of PartyRelationship', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('partyRelationship').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', partyRelationshipPageUrl);

        partyRelationship = undefined;
      });
    });
  });

  describe('new PartyRelationship page', () => {
    beforeEach(() => {
      cy.visit(partyRelationshipPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PartyRelationship');
    });

    it('should create an instance of PartyRelationship', () => {
      cy.get(`[data-cy="fromDate"]`).type('2023-12-14T03:02');
      cy.get(`[data-cy="fromDate"]`).blur();
      cy.get(`[data-cy="fromDate"]`).should('have.value', '2023-12-14T03:02');

      cy.get(`[data-cy="thruDate"]`).type('2023-12-14T15:44');
      cy.get(`[data-cy="thruDate"]`).blur();
      cy.get(`[data-cy="thruDate"]`).should('have.value', '2023-12-14T15:44');

      cy.get(`[data-cy="relationshipName"]`).type('degenerate eventually');
      cy.get(`[data-cy="relationshipName"]`).should('have.value', 'degenerate eventually');

      cy.get(`[data-cy="partyFrom"]`).select(1);
      cy.get(`[data-cy="partyTo"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        partyRelationship = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', partyRelationshipPageUrl);
    });
  });
});
