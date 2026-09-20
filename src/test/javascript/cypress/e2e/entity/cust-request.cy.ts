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

describe('CustRequest e2e test', () => {
  const custRequestPageUrl = '/cust-request';
  let username: string;
  let password: string;
  const custRequestSample = { name: 'weary second untidy', custRequestDate: '2023-12-14T02:43:20.558Z' };

  let custRequest;
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
        isOrganisation: true,
        organisationName: 'upset humidity eyeglasses',
        organisationShortName: 'squirm than well-groomed',
        salutation: 'elementary ha restfu',
        firstName: 'Bradford',
        middleName: 'gadzooks',
        lastName: 'Cassin',
        gender: 'MALE',
        birthDate: '2023-12-14',
        primaryPhone: 'yuck',
        primaryEmail: 'whereas urban ack',
        isTemporaryPassword: true,
        logoImageUrl: 'separately',
        profileImageUrl: 'quicker',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/cust-requests+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cust-requests').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cust-requests/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cust-request-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/cust-request-categories', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/product-stores', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/sales-channels', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (custRequest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-requests/${custRequest.id}`,
      }).then(() => {
        custRequest = undefined;
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

  it('CustRequests menu should load CustRequests page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cust-request');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustRequest').should('exist');
    cy.location('pathname').should('eq', custRequestPageUrl);
  });

  describe('CustRequest page', () => {
    it('should have translated page title', () => {
      cy.visit(custRequestPageUrl);
      cy.getEntityHeading('CustRequest').should('not.contain', 'snpApp.custRequest.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(custRequestPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustRequest page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${custRequestPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cust-requests',
          body: {
            ...custRequestSample,
            fromParty: party,
          },
        }).then(({ body }) => {
          custRequest = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cust-requests+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cust-requests?page=0&size=20>; rel="last",<http://localhost/api/cust-requests?page=0&size=20>; rel="first"',
              },
              body: [custRequest],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(custRequestPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CustRequest page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('custRequest');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestPageUrl);
      });

      it('edit button click should load edit CustRequest page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequest');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestPageUrl);
      });

      it('edit button click should load edit CustRequest page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequest');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestPageUrl);
      });

      it('last delete button click should delete instance of CustRequest', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('custRequest').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestPageUrl);

        custRequest = undefined;
      });
    });
  });

  describe('new CustRequest page', () => {
    beforeEach(() => {
      cy.visit(custRequestPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustRequest');
    });

    it('should create an instance of CustRequest', () => {
      cy.get(`[data-cy="name"]`).type('phew');
      cy.get(`[data-cy="name"]`).should('have.value', 'phew');

      cy.get(`[data-cy="description"]`).type('energetic adventurously');
      cy.get(`[data-cy="description"]`).should('have.value', 'energetic adventurously');

      cy.get(`[data-cy="priority"]`).type('16');
      cy.get(`[data-cy="priority"]`).should('have.value', '16');

      cy.get(`[data-cy="custRequestDate"]`).type('2023-12-14T11:18');
      cy.get(`[data-cy="custRequestDate"]`).blur();
      cy.get(`[data-cy="custRequestDate"]`).should('have.value', '2023-12-14T11:18');

      cy.get(`[data-cy="responseRequiredDate"]`).type('2023-12-14T02:35');
      cy.get(`[data-cy="responseRequiredDate"]`).blur();
      cy.get(`[data-cy="responseRequiredDate"]`).should('have.value', '2023-12-14T02:35');

      cy.get(`[data-cy="openDateTime"]`).type('2023-12-14T10:51');
      cy.get(`[data-cy="openDateTime"]`).blur();
      cy.get(`[data-cy="openDateTime"]`).should('have.value', '2023-12-14T10:51');

      cy.get(`[data-cy="closedDateTime"]`).type('2023-12-14T18:31');
      cy.get(`[data-cy="closedDateTime"]`).blur();
      cy.get(`[data-cy="closedDateTime"]`).should('have.value', '2023-12-14T18:31');

      cy.get(`[data-cy="internalComment"]`).type('quirkily syringe pity');
      cy.get(`[data-cy="internalComment"]`).should('have.value', 'quirkily syringe pity');

      cy.get(`[data-cy="billed"]`).should('not.be.checked');
      cy.get(`[data-cy="billed"]`).click();
      cy.get(`[data-cy="billed"]`).should('be.checked');

      cy.get(`[data-cy="custEstimatedDurationDays"]`).type('15171');
      cy.get(`[data-cy="custEstimatedDurationDays"]`).should('have.value', '15171');

      cy.get(`[data-cy="fromParty"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        custRequest = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', custRequestPageUrl);
    });
  });
});
